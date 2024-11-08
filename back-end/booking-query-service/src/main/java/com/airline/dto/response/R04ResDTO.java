package com.airline.dto.response;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.airline.model.BookingQuery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R04ResDTO {
    private UUID bookingId;
    private String bookingDate;
    private String bookingTime;
    private String bookingCode;
    private BigDecimal moneySpent;
    private Integer milesSpent;
    private Integer seatsNumber;
    private String statusDescription;
    private String flightCode;

    public R04ResDTO(BookingQuery entity) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        ZonedDateTime localTimeBooking = entity.getBookingDate().withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

        bookingId = entity.getBookingId();
        bookingDate = localTimeBooking.format(dateFormatter);
        bookingTime = localTimeBooking.format(timeFormatter);
        bookingCode = entity.getBookingCode();
        moneySpent = entity.getMoneySpent();
        milesSpent = entity.getMilesSpent();
        seatsNumber = entity.getNumberOfSeats();
        statusDescription = entity.getStatusDescription();
        flightCode = entity.getFlightCode();
    }
}
