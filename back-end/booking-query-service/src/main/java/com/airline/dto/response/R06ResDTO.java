package com.airline.dto.response;

import java.math.BigDecimal;

import com.airline.model.BookingQuery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R06ResDTO {
    private String flightCode;
    private BigDecimal moneySpent;
    private Integer milesSpent;

    public R06ResDTO(BookingQuery bookingQuery) {
        flightCode = bookingQuery.getFlightCode();
        moneySpent = bookingQuery.getMoneySpent();
        milesSpent = bookingQuery.getMilesSpent();
    }
}
