package bantads.airline.sagas;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.commands.CreateClientCommand;
import bantads.airline.sagas.commands.RefundClientCommand;
import bantads.airline.sagas.commands.UpdateMilesCommand;
import bantads.airline.sagas.events.ClientCreatedEvent;
import bantads.airline.sagas.events.ClientRefundedEvent;
import bantads.airline.sagas.events.MilesUpdatedEvent;
import bantads.airline.sagas.queries.ManageRegisterRes;
import bantads.airline.sagas.queries.VerifyClientQuery;

@Component
public class SagasHandler {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SagaService sagaService;

    @RabbitListener(queues = "ClientRequestChannel")
    public void handleMessage(String msg) throws JsonProcessingException, JsonMappingException {

        Object object = objectMapper.readValue(msg, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            if ("CreateClientCommand".equals(map.get("messageType"))) {

                CreateClientCommand createClientCommand = objectMapper.convertValue(map, CreateClientCommand.class);

                ClientCreatedEvent clientCreatedEvent = sagaService.saveNewClient(createClientCommand);

                var resMsg = objectMapper.writeValueAsString(clientCreatedEvent);

                rabbitTemplate.convertAndSend("ClientReturnChannel", resMsg);

            } else if ("UpdateMilesCommand".equals(map.get("messageType"))) {

                UpdateMilesCommand updateMilesCommand = objectMapper.convertValue(map, UpdateMilesCommand.class);

                MilesUpdatedEvent milesUpdatedEvent = sagaService.updateMiles(updateMilesCommand);

                var resMsg = objectMapper.writeValueAsString(milesUpdatedEvent);

                rabbitTemplate.convertAndSend("ClientReturnChannel", resMsg);

            } else if ("RefundClientCommand".equals(map.get("messageType"))) {

                RefundClientCommand refundClientCommand = objectMapper.convertValue(map, RefundClientCommand.class);

                ClientRefundedEvent clientRefundedEvent = sagaService.refundClient(refundClientCommand);

                var resMsg = objectMapper.writeValueAsString(clientRefundedEvent);

                rabbitTemplate.convertAndSend("ClientReturnChannel", resMsg);

            }

        }
    }

    @RabbitListener(queues = "VerifyClientRequestChannel")
    public void verifyClient(String msg) throws JsonProcessingException, JsonMappingException {

        VerifyClientQuery verifyClientQuery = objectMapper.readValue(msg, VerifyClientQuery.class);

        ManageRegisterRes res = sagaService.verifyClient(verifyClientQuery);

        var resMsg = objectMapper.writeValueAsString(res);

        rabbitTemplate.convertAndSend("VerifyClientReturnChannel", resMsg);
    }
}
