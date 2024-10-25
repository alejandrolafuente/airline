package com.airline.sagas.commands;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.airline.model.BookingQuery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingCommand {
    private UUID bookingCommandId; // * must save as String Type
    private String bookingCode;
    private String flightCode;
    private ZonedDateTime bookingDate;
    // booking status
    private UUID statusCommandId; // * must save as String Type
    private Integer statusCode;
    private String statusAcronym;
    private String statusDescription;
    // *******************************************************
    private BigDecimal moneySpent;
    private Integer milesSpent;
    private Integer numberOfSeats;
    private String userId;
    private String messageType;

    
}
