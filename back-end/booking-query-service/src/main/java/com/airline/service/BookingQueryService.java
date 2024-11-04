package com.airline.service;

import java.util.List;

import com.airline.cqrs.commands.Command;
import com.airline.dto.response.R03ResDTO;

public interface BookingQueryService {

    // R03 from controller
    List<R03ResDTO> findClientBookings(String userId);

    // R10, R12 from cqrs
    void syncronizeDBs(Command command);
}
