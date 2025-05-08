package bantads.airline.sagas.listeners;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.deletemployeesaga.DelEmpSaga;
import bantads.airline.sagas.deletemployeesaga.events.UserDeletedEvent;
import bantads.airline.sagas.registeremployeesaga.RegisterEmployeeSaga;
import bantads.airline.sagas.registeremployeesaga.events.EmpUserCreatedEvent;
import bantads.airline.sagas.selfregistersaga.SelfRegisterSAGA;
import bantads.airline.sagas.selfregistersaga.events.UserCreatedEvent;
import bantads.airline.sagas.updateemployeesaga.UpdateEmpSaga;
import bantads.airline.sagas.updateemployeesaga.events.UpEmpUserEvent;

@Component
public class AuthReturnChannelListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SelfRegisterSAGA selfRegisterSAGA;

    @Autowired
    private RegisterEmployeeSaga registerEmployeeSaga;

    @Autowired
    private UpdateEmpSaga updateEmpSaga;

    @Autowired
    private DelEmpSaga delEmpSaga;

    @RabbitListener(queues = "AuthReturnChannel")
    public void handleAuthResponses(String receivedMessage) throws JsonMappingException, JsonProcessingException {

        Object object = objectMapper.readValue(receivedMessage, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            String messageType = (String) map.get("messageType");

            switch (messageType) {

                case "UserCreatedEvent" -> {
                    UserCreatedEvent event = objectMapper.convertValue(map, UserCreatedEvent.class);
                    selfRegisterSAGA.handleUserCreatedEvent(event);
                    break;
                }

                case "EmployeeUserCreatedEvent" -> {
                    EmpUserCreatedEvent event = objectMapper.convertValue(map, EmpUserCreatedEvent.class);
                    registerEmployeeSaga.handleUserCreatedEvent(event);
                    break;
                }

                // R18
                case "UpEmpUserEvent" -> {
                    UpEmpUserEvent event = objectMapper.convertValue(map, UpEmpUserEvent.class);
                    updateEmpSaga.handleEmpUserUpEvent(event);
                    break;
                }

                // R19
                case "UserDeletedEvent" -> {
                    UserDeletedEvent event = objectMapper.convertValue(map, UserDeletedEvent.class);
                    delEmpSaga.handleuserDeletedEvent(event);
                    break;
                }
            }

        }
    }
}
