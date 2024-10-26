package com.airline.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airline.model.BookingStatus;


@Repository
public interface BookingStatusRep extends JpaRepository<BookingStatus, UUID> {

    BookingStatus findByStatusCode(Integer statusCode);

}
