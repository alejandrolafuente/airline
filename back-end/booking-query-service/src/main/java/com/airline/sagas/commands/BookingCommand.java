package com.airline.sagas.commands;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;


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

    
}
