package com.airline.sagas.commands;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBookingCommand {
    private UUID flightId;
    private String flightCode;
    private BigDecimal moneyValue;
    private Integer usedMiles;
    private Integer totalSeats;
    private String userId;
    private UUID transactionId;
    private String messageType;
}
