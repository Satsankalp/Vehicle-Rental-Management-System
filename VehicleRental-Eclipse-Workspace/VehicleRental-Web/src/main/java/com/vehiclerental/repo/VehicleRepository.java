package com.vehiclerental.repo;

import com.vehiclerental.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByAvailableTrue();

    List<Vehicle> findByMakeContainingIgnoreCaseOrModelContainingIgnoreCase(String make, String model);

    List<Vehicle> findByType(Vehicle.Type type);
}
