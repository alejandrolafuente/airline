package bantads.airline.sagas.listeners;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.selfregistersaga.SelfRegisterSAGA;
import bantads.airline.sagas.selfregistersaga.events.ClientCreatedEvent;

@Component
public class ClientReturnChannelListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SelfRegisterSAGA selfRegisterSAGA;

    @RabbitListener(queues = "ClientReturnChannel")
    public void handleAccountResponses(String receivedMessage) throws JsonMappingException, JsonProcessingException {

        Object object = objectMapper.readValue(receivedMessage, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            String messageType = (String) map.get("messageType");

            switch (messageType) {

                case "ClientCreatedEvent" -> {
                    ClientCreatedEvent clientCreatedEvent = objectMapper.convertValue(map,
                            ClientCreatedEvent.class);

                    selfRegisterSAGA.handleClientCreatedEvent(clientCreatedEvent);

                    break;
                }
            }

        }
    }
}
