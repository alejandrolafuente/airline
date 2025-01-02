package com.airline.sagas;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.airline.sagas.commands.CreateEmployeeCommand;
import com.airline.sagas.events.EmployeeCreatedEvent;
import com.airline.sagas.queries.ManageRegisterRes;
import com.airline.sagas.queries.VerifyEmployeeQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class SagasHandler {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SagaService sagaService;

    // R17
    @RabbitListener(queues = "VerifyEmployeeRequestChannel")
    public void verifyClient(String msg) throws JsonProcessingException, JsonMappingException {

        VerifyEmployeeQuery query = objectMapper.readValue(msg, VerifyEmployeeQuery.class);

        ManageRegisterRes res = sagaService.verifyClient(query);

        var resMsg = objectMapper.writeValueAsString(res);

        rabbitTemplate.convertAndSend("VerifyEmployeeReturnChannel", resMsg);
    }

    @RabbitListener(queues = "EmployeeRequestChannel")
    public void handleMessage(String msg) throws JsonProcessingException, JsonMappingException {

        Object object = objectMapper.readValue(msg, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            String messageType = (String) map.get("messageType");

            switch (messageType) {

                case "CreateEmployeeCommand" -> {

                    CreateEmployeeCommand command = objectMapper.convertValue(map, CreateEmployeeCommand.class);

                    EmployeeCreatedEvent event = sagaService.saveNewEmployee(command);

                    String resMsg = objectMapper.writeValueAsString(event);

                    rabbitTemplate.convertAndSend("EmployeeReturnChannel", resMsg);

                    break;
                }
            }
        }
    }
}
