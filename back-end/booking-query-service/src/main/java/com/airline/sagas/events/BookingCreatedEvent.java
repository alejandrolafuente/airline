package com.airline.sagas.events;

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
public class BookingCreatedEvent {
    private UUID bookingId; // id no bd query command
    private String bookingCommandId;
    private String bookingCode;
    private ZonedDateTime bookingDate;
    private String statusDescription;
}
