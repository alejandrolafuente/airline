package com.airline.service;

import java.util.List;

import com.airline.cqrs.commands.Command;
import com.airline.dto.response.R03ResDTO;
import com.airline.dto.response.R04ResDTO;

public interface BookingQueryService {

    // R03 - 2 from controller
    List<R03ResDTO> findClientBookings(String userId);

    // R04 from controller
    R04ResDTO getBooking(String bookingId);

    // R10, R12 from cqrs
    void syncronizeDBs(Command command);
}
