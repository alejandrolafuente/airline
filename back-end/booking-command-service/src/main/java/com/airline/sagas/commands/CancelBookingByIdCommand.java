package com.airline.sagas.commands;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelBookingByIdCommand {
    private UUID bookingId;
    private String messageType;
}