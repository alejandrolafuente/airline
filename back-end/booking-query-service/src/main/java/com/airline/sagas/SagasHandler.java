package com.airline.sagas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airline.sagas.commands.BookingCommand;
import com.airline.sagas.events.BookingCreatedEvent;

@Component
public class SagasHandler {

    @Autowired
    private SagaService sagaService;

    public void handleBookingCommand(BookingCommand bookingCommand) {

        BookingCreatedEvent bookingCreatedEvent = sagaService.insertBooking(bookingCommand);
        // imprimir aqui
        System.out.println("O ULTIMO QUE SALVAMOS AQUI EM QUERY SERVICE: " + bookingCreatedEvent);

    }

}
