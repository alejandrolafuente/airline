package bantads.airline.sagas;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.commands.CreaEmpUserCommand;
import bantads.airline.sagas.commands.CreateUserCommand;
import bantads.airline.sagas.commands.DeleteUserCommand;
import bantads.airline.sagas.commands.UpEmpUserCommand;
import bantads.airline.sagas.events.UpEmpUserEvent;
import bantads.airline.sagas.events.UserCreatedEvent;
import bantads.airline.sagas.events.UserDeletedEvent;
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

            String messageType = (String) map.get("messageType");

            switch (messageType) {

                // R01
                case "CreateUserCommand" -> {

                    CreateUserCommand command = objectMapper.convertValue(map, CreateUserCommand.class);

                    UserCreatedEvent event = authService.saveNewUserRequest(command);

                    var resMsg = objectMapper.writeValueAsString(event);

                    rabbitTemplate.convertAndSend("AuthReturnChannel", resMsg);

                    break;
                }

                case "CreateEmployeeUserCommand" -> {

                    CreaEmpUserCommand command = objectMapper.convertValue(map, CreaEmpUserCommand.class);

                    UserCreatedEvent event = authService.newEmpUserRequest(command);

                    var resMsg = objectMapper.writeValueAsString(event);

                    rabbitTemplate.convertAndSend("AuthReturnChannel", resMsg);

                    break;
                }

                //R18
                case "UpEmpUserCommand" -> {

                    UpEmpUserCommand command = objectMapper.convertValue(map, UpEmpUserCommand.class);

                    UpEmpUserEvent event = authService.updateEmployee(command);

                    var resMsg = objectMapper.writeValueAsString(event);

                    rabbitTemplate.convertAndSend("AuthReturnChannel", resMsg);

                    break;
                }

                 //R19
                 case "DeleteUserCommand" -> {

                    DeleteUserCommand command = objectMapper.convertValue(map, DeleteUserCommand.class);

                    UserDeletedEvent event = authService.deleteEmployee(command);

                    var resMsg = objectMapper.writeValueAsString(event);

                    rabbitTemplate.convertAndSend("AuthReturnChannel", resMsg);

                    break;
                }

            }
        }

    }
}
