package bantads.airline.sagas;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.commands.CreateUserCommand;
import bantads.airline.sagas.events.UserCreatedEvent;
import bantads.airline.securityservice.AuthService;

@Component
public class SagasHandler {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @RabbitListener(queues = "AuthRequestChannel")
    public void handleMessage(String msg) throws JsonProcessingException, JsonMappingException {

        Object object = objectMapper.readValue(msg, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            if ("CreateUserCommand".equals(map.get("messageType"))) {

                CreateUserCommand createUserCommand = objectMapper.convertValue(map, CreateUserCommand.class);

                UserCreatedEvent userCreatedEvent = authService.saveNewUserRequest(createUserCommand);

                var resMsg = objectMapper.writeValueAsString(userCreatedEvent);

                rabbitTemplate.convertAndSend("AuthReturnChannel", resMsg);
            }

        }

    }
}
