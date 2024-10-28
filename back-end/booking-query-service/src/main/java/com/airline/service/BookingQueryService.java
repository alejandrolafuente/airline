package com.airline.service;

import java.util.List;

import com.airline.cqrs.commands.DoCheckInCommand;
import com.airline.dto.response.R03ResDTO;

public interface BookingQueryService {
    // R03 from controller
    List<R03ResDTO> findBookedFlights(String userId);

    // R10 from cqrs
    void doCheckIn(DoCheckInCommand doCheckInCommand);
}
