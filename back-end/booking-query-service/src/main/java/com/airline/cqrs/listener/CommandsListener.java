package com.airline.cqrs.listener;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airline.cqrs.commands.Command;
import com.airline.sagas.SagasHandler;
import com.airline.sagas.commands.BookingCommand;
import com.airline.service.BookingQueryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CommandsListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SagasHandler sagasHandler;

    @Autowired
    private BookingQueryService bookingQueryService;

    @RabbitListener(queues = "BookingQueryRequestChannel")
    public void handleCommands(String message) throws JsonMappingException, JsonProcessingException {

        Object object = objectMapper.readValue(message, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            String messageType = (String) map.get("messageType");

            switch (messageType) {

                case "BookingCommand" -> {// mudar para chamar direto o serviço e eliminar o sagasHandler
                    BookingCommand bookingCommand = objectMapper.convertValue(map, BookingCommand.class);
                    sagasHandler.handleBookingCommand(bookingCommand);
                }

                case "SynCommand" -> {
                    
                    Command command = objectMapper.convertValue(map, Command.class);

                    bookingQueryService.syncronizeDBs(command);
                }

                default -> {
                    // you can handle unknown message types or log an error
                }

            }
        }

    }
}
