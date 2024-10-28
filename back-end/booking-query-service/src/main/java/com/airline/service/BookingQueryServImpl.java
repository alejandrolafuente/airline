package com.airline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.cqrs.commands.DoCheckInCommand;
import com.airline.dto.response.R03ResDTO;
import com.airline.model.BookingQuery;
import com.airline.repository.BookingQueryRepository;

@Service
public class BookingQueryServImpl implements BookingQueryService {

    @Autowired
    private BookingQueryRepository bookingQueryRepository;

    // R03
    @Override
    public List<R03ResDTO> findBookedFlights(String userId) {

        List<BookingQuery> bookedFlights = bookingQueryRepository.findByStatusCodeAndUserId(1, userId);

        return bookedFlights.stream().map(R03ResDTO::new).toList();
    }

    // R10 from cqrs
    @Override
    public void doCheckIn(DoCheckInCommand doCheckInCommand) {

        
    }

}
