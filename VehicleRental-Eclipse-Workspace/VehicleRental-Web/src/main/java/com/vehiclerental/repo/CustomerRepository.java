package com.vehiclerental.repo;

import com.vehiclerental.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByLicenseNoIgnoreCase(String licenseNo);
}
