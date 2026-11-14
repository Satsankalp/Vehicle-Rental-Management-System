package com.vehiclerental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    public enum Type { CAR, BIKE, VAN, TRUCK }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String make;

    @NotBlank
    private String model;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type;

    @DecimalMin(value = "0.01", message = "Rate must be positive")
    private double dailyRate;

    private boolean available = true;

    protected Vehicle() { /* JPA */ }

    public Vehicle(String make, String model, Type type, double dailyRate) {
        this.make = make;
        this.model = model;
        this.type = type;
        this.dailyRate = dailyRate;
    }

    public Long getId() { return id; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public Type getType() { return type; }
    public double getDailyRate() { return dailyRate; }
    public void setDailyRate(double dailyRate) { this.dailyRate = dailyRate; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    /** Display id like the legacy CLI (V001, V002, ...). */
    public String getCode() { return String.format("V%03d", id == null ? 0 : id); }

    @Override
    public String toString() {
        return String.format("%s %-5s %-10s %-12s %8.2f/day  %s",
                getCode(), type, make, model, dailyRate, available ? "Available" : "Rented");
    }
}
