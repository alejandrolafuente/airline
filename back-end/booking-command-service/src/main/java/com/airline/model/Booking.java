package com.airline.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "booking_table")
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_id", nullable = false, updatable = false)
    private UUID bookingId;

    @Column(name = "booking_code", nullable = false, unique = true, updatable = false)
    private String bookingCode;

    @Column(name = "flight_code", nullable = false)
    private String flightCode;

    @Column(name = "booking_date", nullable = false)
    private ZonedDateTime bookingDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_status", referencedColumnName = "status_id", nullable = false)
    private BookingStatus bookingStatus;

    @Column(name = "money_spent", nullable = true)
    private BigDecimal moneySpent;

    @Column(name = "miles_spent")
    private Integer milesSpent;

    @Column(name = "seats_number", nullable = false)
    private Integer numberOfSeats;

    @Column(name = "user_id", nullable = false)
    private String userId;
}
