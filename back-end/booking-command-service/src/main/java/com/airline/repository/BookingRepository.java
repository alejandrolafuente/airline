package com.airline.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.airline.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("select b from Booking b where b.bookingCode = ?1")
    Optional<Booking> getBookingByCode(String bookingCode);

    // R14
    List<Booking> findByFlightCode(String flightCode);

}