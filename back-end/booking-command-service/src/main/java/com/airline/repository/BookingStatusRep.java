package com.airline.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.airline.model.BookingStatus;

public interface BookingStatusRep extends JpaRepository<BookingStatus, UUID> {

    BookingStatus findByStatusCode(Integer statusCode);

}
