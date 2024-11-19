package com.airline.sagas;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airline.sagas.commands.CancelBookingByIdCommand;
import com.airline.sagas.commands.CancelBookingsCommand;
import com.airline.sagas.commands.CompleteBookingsCommand;
import com.airline.sagas.commands.CreateBookingCommand;
import com.airline.sagas.events.BookingCanByIdEvent;
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

                    CreateBookingCommand command = objectMapper.convertValue(map,
                            CreateBookingCommand.class);

                    BookingCreatedEvent event = sagaService.insertBooking(command);

                    var message = objectMapper.writeValueAsString(event);

                    rabbitTemplate.convertAndSend("BookingCommandReturnChannel", message);
                    break;
                }

                case "CompleteBookingsCommand" -> {

                    CompleteBookingsCommand command = objectMapper.convertValue(map, CompleteBookingsCommand.class);

                    BookingsCompletedEvent event = sagaService.completeBookings(command);

                    var message = objectMapper.writeValueAsString(event);

                    rabbitTemplate.convertAndSend("BookingCommandReturnChannel", message);
                    break;
                }
                
                // R13 - 2
                case "CancelBookingsCommand" -> { // using Flight Code

                    CancelBookingsCommand command = objectMapper.convertValue(map,
                            CancelBookingsCommand.class);

                    BookingCancelledEvent event = sagaService.cancelBookings(command);

                    var message = objectMapper.writeValueAsString(event);

                    rabbitTemplate.convertAndSend("BookingCommandReturnChannel", message);
                    break;
                }

                case "CancelBookingByIdCommand" -> {

                    CancelBookingByIdCommand command = objectMapper.convertValue(map,
                            CancelBookingByIdCommand.class);

                    BookingCanByIdEvent event = sagaService.cancelSingleBooking(command);

                    var message = objectMapper.writeValueAsString(event);

                    rabbitTemplate.convertAndSend("BookingCommandReturnChannel", message);
                    break;

                }
            }
        }
    }
}
