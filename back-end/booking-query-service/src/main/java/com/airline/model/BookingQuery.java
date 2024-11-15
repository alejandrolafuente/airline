package com.airline.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.airline.sagas.commands.BookingCommand;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "booking_query_table")
public class BookingQuery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_id", nullable = false, updatable = false)
    private UUID bookingId;

    @Column(name = "booking_command_id", nullable = false)
    private UUID bookingCommandId; // * id in booking command service

    @Column(name = "booking_code", nullable = false)
    private String bookingCode;

    @Column(name = "flight_code", nullable = false)
    private String flightCode;

    @Column(name = "booking_date", nullable = false)
    private ZonedDateTime bookingDate;

    // booking status
    @Column(name = "status_command_id", nullable = false)
    private UUID statusCommandId; // * id in booking command service

    @Column(name = "status_code", nullable = false)
    private Integer statusCode;

    @Column(name = "status_acronym", nullable = false)
    private String statusAcronym;

    @Column(name = "status_description", nullable = false)
    private String statusDescription;
    // *******************************************************

    @Column(name = "money_spent", nullable = false)
    private BigDecimal moneySpent;

    @Column(name = "miles_spent", nullable = false)
    private Integer milesSpent;

    @Column(name = "seats_number", nullable = false)
    private Integer numberOfSeats;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "transaction_id", nullable = false)
    private UUID transactionId;

    // constructor
    public BookingQuery(BookingCommand bookingCommand) {

        this.bookingCommandId = bookingCommand.getBookingCommandId();
        this.bookingCode = bookingCommand.getBookingCode();
        this.flightCode = bookingCommand.getFlightCode();
        this.bookingDate = bookingCommand.getBookingDate();
        this.statusCommandId = bookingCommand.getStatusCommandId();
        this.statusCode = bookingCommand.getStatusCode();
        this.statusAcronym = bookingCommand.getStatusAcronym();
        this.statusDescription = bookingCommand.getStatusDescription();
        this.moneySpent = bookingCommand.getMoneySpent();
        this.milesSpent = bookingCommand.getMilesSpent();
        this.numberOfSeats = bookingCommand.getNumberOfSeats();
        this.userId = bookingCommand.getUserId();
        this.transactionId = bookingCommand.getTransactionId();
    }
}
