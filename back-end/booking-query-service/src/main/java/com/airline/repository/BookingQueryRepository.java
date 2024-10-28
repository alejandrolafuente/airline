package com.airline.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airline.model.BookingQuery;

@Repository
public interface BookingQueryRepository extends JpaRepository<BookingQuery, UUID> {

    List<BookingQuery> findByStatusCodeAndUserId(Integer statusCode, String userId);

    BookingQuery findByBookingCommandId(String bookingCommandId);
}
