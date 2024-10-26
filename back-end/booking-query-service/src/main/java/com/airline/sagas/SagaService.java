package com.airline.sagas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.model.BookingQuery;
import com.airline.repository.BookingQueryRepository;
import com.airline.sagas.commands.BookingCommand;
import com.airline.sagas.events.BookingCreatedEvent;

@Service
public class SagaService {

    @Autowired
    private BookingQueryRepository bookingQueryRepository;

    public BookingCreatedEvent insertBooking(BookingCommand bookingCommand) {

        BookingQuery newBooking = new BookingQuery(bookingCommand);

        newBooking = bookingQueryRepository.save(newBooking);

        BookingCreatedEvent bookingCreatedEvent = BookingCreatedEvent.builder()
                .bookingId(newBooking.getBookingId())
                .bookingCommandId(newBooking.getBookingCommandId())
                .bookingCode(newBooking.getBookingCode())
                .bookingDate(newBooking.getBookingDate()) // converter para hora local com ajuda de flight service
                .statusDescription(newBooking.getStatusDescription())
                .build();

        return bookingCreatedEvent;
        // imprimimos a resposta no handler
    }

}
