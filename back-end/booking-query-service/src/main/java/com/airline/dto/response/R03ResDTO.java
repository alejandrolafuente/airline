package com.airline.dto.response;

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
public class R03ResDTO {
    private UUID bookingId;
    private String bookingCode; // request key for flight service
    private String flightCode;
    private String statusDescription;

    public R03ResDTO(BookingQuery entity) {
        bookingId = entity.getBookingId();
        bookingCode = entity.getBookingCode();
        flightCode = entity.getFlightCode();
        statusDescription = entity.getStatusDescription();
    }
}
