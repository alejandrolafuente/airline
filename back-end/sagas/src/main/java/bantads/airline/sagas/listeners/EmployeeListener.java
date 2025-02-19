package bantads.airline.sagas.listeners;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.deletemployeesaga.DelEmpSaga;
import bantads.airline.sagas.deletemployeesaga.events.EmpDeletedEvent;
import bantads.airline.sagas.registeremployeesaga.RegisterEmployeeSaga;
import bantads.airline.sagas.registeremployeesaga.events.EmployeeCreatedEvent;
import bantads.airline.sagas.updateemployeesaga.UpdateEmpSaga;
import bantads.airline.sagas.updateemployeesaga.events.EmpUpdatedEvent;

@Component
public class EmployeeListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegisterEmployeeSaga registerEmployeeSaga;

    @Autowired
    private UpdateEmpSaga updateEmpSaga;

    @Autowired
    private DelEmpSaga delEmpSaga;

    @RabbitListener(queues = "EmployeeReturnChannel")
    public void handleAuthResponses(String receivedMessage) throws JsonMappingException, JsonProcessingException {

        Object object = objectMapper.readValue(receivedMessage, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            String messageType = (String) map.get("messageType");

            switch (messageType) {

                case "EmployeeCreatedEvent" -> {
                    EmployeeCreatedEvent event = objectMapper.convertValue(map, EmployeeCreatedEvent.class);
                    registerEmployeeSaga.handleEmployeeCreatedEvent(event);
                    break;
                }

                case "EmpUpdatedEvent" -> {
                    EmpUpdatedEvent event = objectMapper.convertValue(map, EmpUpdatedEvent.class);
                    updateEmpSaga.handleEmpUpdatedEvent(event);
                    break;
                }

                case "EmpDeletedEvent" -> {
                    EmpDeletedEvent event = objectMapper.convertValue(map, EmpDeletedEvent.class);
                    delEmpSaga.handleEmpDeletedEvent(event);
                    break;
                }
                    
            }
        }

    }
}
