package com.vehiclerental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate dueDate;

    private double dailyRate;

    private LocalDate returnDate;

    private double lateFee;

    protected Rental() { /* JPA */ }

    public Rental(Vehicle vehicle, Customer customer, LocalDate startDate, LocalDate dueDate, double dailyRate) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.dailyRate = dailyRate;
    }

    public Long getId() { return id; }
    public Vehicle getVehicle() { return vehicle; }
    public Customer getCustomer() { return customer; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public double getLateFee() { return lateFee; }
    public boolean isActive() { return returnDate == null; }

    public long plannedDays() {
        return Math.max(1, ChronoUnit.DAYS.between(startDate, dueDate));
    }

    public double baseCost() { return plannedDays() * dailyRate; }
    public double totalCost() { return baseCost() + lateFee; }

    /** Closes the rental. Each overdue day is charged at 150% of the daily rate. */
    public void close(LocalDate when) {
        this.returnDate = when;
        long lateDays = ChronoUnit.DAYS.between(dueDate, when);
        this.lateFee = lateDays > 0 ? lateDays * dailyRate * 1.5 : 0;
    }

    /** Display id like the legacy CLI (R001, R002, ...). */
    public String getCode() { return String.format("R%03d", id == null ? 0 : id); }

    @Override
    public String toString() {
        return String.format("%s Vehicle:%s Customer:%s %s -> %s  Cost: %.2f  %s",
                getCode(), vehicle.getCode(), customer.getCode(), startDate, dueDate, totalCost(),
                isActive() ? "[ACTIVE]" : "[RETURNED " + returnDate + (lateFee > 0 ? ", late fee " + String.format("%.2f", lateFee) : "") + "]");
    }
}
