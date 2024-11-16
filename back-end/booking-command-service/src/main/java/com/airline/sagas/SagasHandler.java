package com.airline.sagas;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airline.sagas.commands.CancelBookingByIdCommand;
import com.airline.sagas.commands.CancelBookingCommand;
import com.airline.sagas.commands.CompleteBookingCommand;
import com.airline.sagas.commands.CreateBookingCommand;
import com.airline.sagas.events.BookingCancelledEvent;
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

            String messageType = (String) map.get("messageType");

            switch (messageType) {

                case "CreateBookingCommand" -> {

                    CreateBookingCommand createBookingCommand = objectMapper.convertValue(map,
                            CreateBookingCommand.class);

                    BookingCreatedEvent bookingCreatedEvent = sagaService.insertBooking(createBookingCommand);

                    var message = objectMapper.writeValueAsString(bookingCreatedEvent);

                    rabbitTemplate.convertAndSend("BookingCommandReturnChannel", message);
                    break;
                }

                case "CompleteBookingCommand" -> {

                    CompleteBookingCommand completeBookingCommand = objectMapper.convertValue(map,
                            CompleteBookingCommand.class);

                    BookingsCompletedEvent bookingsCompletedEvent = sagaService
                            .completeBookings(completeBookingCommand);

                    var message = objectMapper.writeValueAsString(bookingsCompletedEvent);

                    rabbitTemplate.convertAndSend("BookingCommandReturnChannel", message);
                    break;
                }

                case "CancelBookingCommand" -> {

                    CancelBookingCommand cancelBookingCommand = objectMapper.convertValue(map,
                            CancelBookingCommand.class);

                    BookingCancelledEvent bookingCancelledEvent = sagaService.cancelBookings(cancelBookingCommand);

                    var message = objectMapper.writeValueAsString(bookingCancelledEvent);

                    rabbitTemplate.convertAndSend("BookingCommandReturnChannel", message);
                    break;
                }

                case "CancelBookingByIdCommand" -> {

                    CancelBookingByIdCommand cancelBookingByIdCommand = objectMapper.convertValue(map,
                            CancelBookingByIdCommand.class);

                    break;
                }
            }
        }
    }
}
