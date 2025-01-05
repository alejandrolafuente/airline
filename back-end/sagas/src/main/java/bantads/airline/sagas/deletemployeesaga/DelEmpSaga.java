package bantads.airline.sagas.deletemployeesaga;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.deletemployeesaga.commands.DeleteEmpCommand;
import bantads.airline.sagas.deletemployeesaga.commands.DeleteUserCommand;
import bantads.airline.sagas.deletemployeesaga.events.EmpDeletedEvent;
import bantads.airline.sagas.deletemployeesaga.events.UserDeletedEvent;

@Component
public class DelEmpSaga {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void handleRequest(String userId) throws JsonProcessingException {

        DeleteUserCommand command = DeleteUserCommand.builder()
                .userId(userId)
                .messageType("DeleteUserCommand")
                .build();

        var message = objectMapper.writeValueAsString(command);

        rabbitTemplate.convertAndSend("AuthRequestChannel", message);
    }

    public void handleuserDeletedEvent(UserDeletedEvent event) throws JsonProcessingException {

        DeleteEmpCommand command = DeleteEmpCommand.builder()
                .userId(event.getUserId())
                .userStatus(event.getUserStatus())
                .messageType("DeleteEmpCommand")
                .build();

        var message = objectMapper.writeValueAsString(command);

        rabbitTemplate.convertAndSend("EmployeeRequestChannel", message);
    }

    public void handleEmpDeletedEvent(EmpDeletedEvent event) {
        System.out.println("Data from deleted Employee:");
        System.out.println("User ID: " + event.getUserId());
        System.out.println("Name:    " + event.getName());
        System.out.println("Status:  " + event.getEmployeeStatus());
    }

}
