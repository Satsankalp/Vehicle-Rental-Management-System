package com.vehiclerental.repo;

import com.vehiclerental.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByReturnDateIsNull();

    List<Rental> findByReturnDateIsNotNull();

    /** Active rentals whose due date is before the given date (overdue). */
    List<Rental> findByReturnDateIsNullAndDueDateBefore(LocalDate date);
}
