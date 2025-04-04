package com.airline.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airline.model.BookingQuery;

@Repository
public interface BookingQueryRepository extends JpaRepository<BookingQuery, UUID> {

    Optional<List<BookingQuery>> findByUserId(String userId);

    BookingQuery findByBookingCommandId(UUID bookingCommandId);

    Optional<BookingQuery> findByTransactionId(UUID transactionId);

    Optional<BookingQuery> findByBookingCode(String bookingCode);
}
