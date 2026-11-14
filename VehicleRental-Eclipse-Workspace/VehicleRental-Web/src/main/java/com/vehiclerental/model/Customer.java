package com.vehiclerental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(name = "uk_customer_license", columnNames = "license_no"))
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String phone;

    @NotBlank
    @Column(name = "license_no")
    private String licenseNo;

    protected Customer() { /* JPA */ }

    public Customer(String name, String phone, String licenseNo) {
        this.name = name;
        this.phone = phone;
        this.licenseNo = licenseNo;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getLicenseNo() { return licenseNo; }

    /** Display id like the legacy CLI (C001, C002, ...). */
    public String getCode() { return String.format("C%03d", id == null ? 0 : id); }

    @Override
    public String toString() {
        return String.format("%s %-18s %-14s License: %s", getCode(), name, phone, licenseNo);
    }
}
