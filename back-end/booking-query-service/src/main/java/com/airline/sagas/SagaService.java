package com.airline.sagas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.model.BookingQuery;
import com.airline.repository.BookingQueryRepository;
import com.airline.sagas.commands.BookingCommand;

@Service
public class SagaService {

    @Autowired
    private BookingQueryRepository bookingQueryRepository;

    public void insertBooking(BookingCommand bookingCommand) {

        BookingQuery newBooking = new BookingQuery(bookingCommand);

        bookingQueryRepository.save(newBooking);

    }

}
