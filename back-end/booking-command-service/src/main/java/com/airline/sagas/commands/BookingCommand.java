package com.airline.sagas.commands;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.airline.model.Booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingCommand {
    private UUID bookingCommandId;
    private String bookingCode;
    private String flightCode;
    private ZonedDateTime bookingDate;
    // booking status
    private UUID statusCommandId;
    private Integer statusCode;
    private String statusAcronym;
    private String statusDescription;
    // *******************************************************
    private BigDecimal moneySpent;
    private Integer milesSpent;
    private Integer numberOfSeats;
    private String userId;
    private UUID transactionId;
    private String messageType;

    public BookingCommand(Booking booking) {
        bookingCommandId = booking.getBookingId();
        bookingCode = booking.getBookingCode();
        flightCode = booking.getFlightCode();
        bookingDate = booking.getBookingDate();
        statusCommandId = booking.getBookingStatus().getStatusId();
        statusCode = booking.getBookingStatus().getStatusCode();
        statusAcronym = booking.getBookingStatus().getStatusAcronym();
        statusDescription = booking.getBookingStatus().getStatusDescription();
        moneySpent = booking.getMoneySpent();
        milesSpent = booking.getMilesSpent();
        numberOfSeats = booking.getNumberOfSeats();
        userId = booking.getUserId();
        transactionId = booking.getTransactionId();
        messageType = "BookingCommand";
    }
}
