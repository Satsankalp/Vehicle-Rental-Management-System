package com.vehiclerental.service;

import com.vehiclerental.model.Customer;
import com.vehiclerental.model.Rental;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.repo.CustomerRepository;
import com.vehiclerental.repo.RentalRepository;
import com.vehiclerental.repo.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Business logic ported from the legacy console app's RentalService
 * (src/rental/RentalService.java), now backed by MySQL via JPA.
 */
@Service
public class RentalService {

    private final VehicleRepository vehicles;
    private final CustomerRepository customers;
    private final RentalRepository rentals;

    public RentalService(VehicleRepository vehicles, CustomerRepository customers, RentalRepository rentals) {
        this.vehicles = vehicles;
        this.customers = customers;
        this.rentals = rentals;
    }

    // ---------- Vehicles ----------
    @Transactional
    public Vehicle addVehicle(String make, String model, Vehicle.Type type, double rate) {
        if (rate <= 0) throw new IllegalArgumentException("Rate must be positive.");
        Vehicle v = new Vehicle(make, model, type, rate);
        return vehicles.save(v);
    }

    @Transactional
    public void removeVehicle(Long id) {
        Vehicle v = requireVehicle(id);
        if (!v.isAvailable()) throw new IllegalStateException("Cannot remove a vehicle that is currently rented.");
        vehicles.delete(v);
    }

    public List<Vehicle> listVehicles() { return vehicles.findAll(); }

    public List<Vehicle> availableVehicles() { return vehicles.findByAvailableTrue(); }

    public List<Vehicle> searchVehicles(String keyword) {
        List<Vehicle> result = new ArrayList<>(vehicles.findByMakeContainingIgnoreCaseOrModelContainingIgnoreCase(keyword, keyword));
        try {
            Vehicle.Type t = Vehicle.Type.valueOf(keyword.trim().toUpperCase());
            for (Vehicle v : vehicles.findByType(t)) {
                if (!result.contains(v)) result.add(v);
            }
        } catch (IllegalArgumentException ignored) {
            // keyword is not a vehicle type name — fine
        }
        return result;
    }

    public Vehicle getVehicle(Long id) { return requireVehicle(id); }

    // ---------- Customers ----------
    @Transactional
    public Customer registerCustomer(String name, String phone, String license) {
        if (name == null || name.isBlank() || license == null || license.isBlank())
            throw new IllegalArgumentException("Name and license are required.");
        customers.findByLicenseNoIgnoreCase(license).ifPresent(c -> {
            throw new IllegalArgumentException("A customer with this license already exists.");
        });
        return customers.save(new Customer(name, phone, license));
    }

    public List<Customer> listCustomers() { return customers.findAll(); }

    public Customer getCustomer(Long id) { return requireCustomer(id); }

    // ---------- Rentals ----------
    @Transactional
    public Rental rentVehicle(Long vehicleId, Long customerId, int days) {
        if (days < 1) throw new IllegalArgumentException("Rental must be at least 1 day.");
        Vehicle v = requireVehicle(vehicleId);
        requireCustomer(customerId);
        if (!v.isAvailable()) throw new IllegalStateException("Vehicle " + v.getCode() + " is already rented.");
        LocalDate today = LocalDate.now();
        Rental r = new Rental(v, requireCustomer(customerId), today, today.plusDays(days), v.getDailyRate());
        v.setAvailable(false);
        vehicles.save(v);
        return rentals.save(r);
    }

    @Transactional
    public Rental returnVehicle(Long rentalId, LocalDate returnDate) {
        Rental r = rentals.findById(rentalId)
                .orElseThrow(() -> new NoSuchElementException("Rental not found: R" + String.format("%03d", rentalId)));
        if (!r.isActive()) throw new IllegalStateException("Rental already closed.");
        r.close(returnDate);
        r.getVehicle().setAvailable(true);
        return r;
    }

    public List<Rental> listRentals() { return rentals.findAll(); }

    public List<Rental> activeRentals() { return rentals.findByReturnDateIsNull(); }

    public List<Rental> returnedRentals() { return rentals.findByReturnDateIsNotNull(); }

    public List<Rental> overdueRentals() {
        return rentals.findByReturnDateIsNullAndDueDateBefore(LocalDate.now());
    }

    public double totalRevenue() {
        return rentals.findByReturnDateIsNotNull().stream().mapToDouble(Rental::totalCost).sum();
    }

    // ---------- Helpers ----------
    private Vehicle requireVehicle(Long id) {
        return vehicles.findById(id).orElseThrow(() -> new NoSuchElementException("Vehicle not found: V" + String.format("%03d", id)));
    }

    private Customer requireCustomer(Long id) {
        return customers.findById(id).orElseThrow(() -> new NoSuchElementException("Customer not found: C" + String.format("%03d", id)));
    }
}
