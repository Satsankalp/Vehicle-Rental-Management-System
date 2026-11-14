package com.vehiclerental.config;

import com.vehiclerental.model.Customer;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.repo.CustomerRepository;
import com.vehiclerental.repo.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Seed data (same fleet as the legacy console app). */
@Configuration
public class SeedData {

    @Bean
    CommandLineRunner seed(VehicleRepository vehicles, CustomerRepository customers) {
        return args -> {
            if (vehicles.count() == 0) {
                vehicles.save(new Vehicle("Toyota", "Corolla", Vehicle.Type.CAR, 45));
                vehicles.save(new Vehicle("Honda", "Civic", Vehicle.Type.CAR, 50));
                vehicles.save(new Vehicle("Yamaha", "R15", Vehicle.Type.BIKE, 20));
                vehicles.save(new Vehicle("Ford", "Transit", Vehicle.Type.VAN, 80));
                vehicles.save(new Vehicle("Tata", "Ace", Vehicle.Type.TRUCK, 95));
            }
            if (customers.count() == 0) {
                customers.save(new Customer("Demo Customer", "9000000000", "DL-0001"));
            }
        };
    }
}
