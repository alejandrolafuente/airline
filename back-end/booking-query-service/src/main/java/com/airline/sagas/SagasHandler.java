package com.airline.sagas;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airline.sagas.commands.BookingCommand;
import com.airline.sagas.events.BookingCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SagasHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SagaService sagaService;

    @RabbitListener(queues = "BookingQueryRequestChannel")
    public void handleMessage(String msg) throws JsonProcessingException, JsonMappingException {

        Object object = objectMapper.readValue(msg, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            if ("BookingCommand".equals(map.get("messageType"))) {
                BookingCommand bookingCommand = objectMapper.convertValue(map, BookingCommand.class);
                BookingCreatedEvent bookingCreatedEvent = sagaService.insertBooking(bookingCommand);
                // imprimir aqui
                System.out.println("O ULTIMO QUE SALVAMOS AQUI EM QUERY SERVICE: " + bookingCreatedEvent);
            }
        }
    }

}
