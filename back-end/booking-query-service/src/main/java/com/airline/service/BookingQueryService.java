package com.airline.service;

import java.util.List;

import com.airline.dto.response.R03ResDTO;

public interface BookingQueryService {
    // R03
    List<R03ResDTO> findBookedFlights(String userId);
}
