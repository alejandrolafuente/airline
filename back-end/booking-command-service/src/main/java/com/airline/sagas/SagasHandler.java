package com.airline.sagas;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airline.sagas.commands.CompleteBookingCommand;
import com.airline.sagas.commands.CreateBookingCommand;
import com.airline.sagas.events.BookingCreatedEvent;
import com.airline.sagas.events.BookingsCompletedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SagasHandler {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SagaService sagaService;

    @RabbitListener(queues = "BookingCommandRequestChannel")
    public void handleMessage(String msg) throws JsonProcessingException, JsonMappingException {

        Object object = objectMapper.readValue(msg, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            if ("CreateBookingCommand".equals(map.get("messageType"))) {

                CreateBookingCommand createBookingCommand = objectMapper.convertValue(map, CreateBookingCommand.class);

                BookingCreatedEvent bookingCreatedEvent = sagaService.insertBooking(createBookingCommand);

                var message = objectMapper.writeValueAsString(bookingCreatedEvent);

                rabbitTemplate.convertAndSend("BookingCommandReturnChannel", message);

            } else if ("CompleteBookingCommand".equals(map.get("messageType"))) {

                CompleteBookingCommand completeBookingCommand = objectMapper.convertValue(map,
                        CompleteBookingCommand.class);

                BookingsCompletedEvent bookingsCompletedEvent = sagaService.completeBookings(completeBookingCommand);

                var message = objectMapper.writeValueAsString(bookingsCompletedEvent);

                rabbitTemplate.convertAndSend("BookingCommandReturnChannel", message);
            }

        }
    }

}
