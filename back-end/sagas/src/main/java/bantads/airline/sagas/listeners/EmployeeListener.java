package bantads.airline.sagas.listeners;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.registeremployeesaga.RegisterEmployeeSaga;
import bantads.airline.sagas.selfregistersaga.events.UserCreatedEvent;

@Component
public class EmployeeListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegisterEmployeeSaga registerEmployeeSaga;

    @RabbitListener(queues = "EmployeeReturnChannel")
    public void handleAuthResponses(String receivedMessage) throws JsonMappingException, JsonProcessingException {

        Object object = objectMapper.readValue(receivedMessage, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            String messageType = (String) map.get("messageType");

            switch (messageType) {

                case "EmployeeCreatedEvent" -> {
                    // UserCreatedEvent event = objectMapper.convertValue(map, UserCreatedEvent.class);
                    // selfRegisterSAGA.handleUserCreatedEvent(event);
                    break;
                }
            }
        }

    }
}
