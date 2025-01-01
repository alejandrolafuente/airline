package com.airline.sagas;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

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

        // VerifyClientQuery verifyClientQuery = objectMapper.readValue(msg,
        // VerifyClientQuery.class);
        VerifyEmployeeQuery query = objectMapper.readValue(msg, VerifyEmployeeQuery.class);

        ManageRegisterRes res = sagaService.verifyClient(query);

        var resMsg = objectMapper.writeValueAsString(res);

        rabbitTemplate.convertAndSend("VerifyEmployeeReturnChannel", resMsg);
    }

}
