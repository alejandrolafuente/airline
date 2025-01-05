package bantads.airline.sagas.updateemployeesaga;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.dto.request.PutEmpDTO;
import bantads.airline.sagas.updateemployeesaga.commands.UpEmpUserCommand;
import bantads.airline.sagas.updateemployeesaga.commands.UpdateEmployeeCommand;
import bantads.airline.sagas.updateemployeesaga.events.EmpUpdatedEvent;
import bantads.airline.sagas.updateemployeesaga.events.UpEmpUserEvent;

@Component
public class UpdateEmpSaga {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String userID;

    public void handleRequest(PutEmpDTO dto) throws JsonProcessingException {

        this.userID = dto.getUserID();

        UpdateEmployeeCommand command = new UpdateEmployeeCommand(dto);

        // 1. vai para o serviço Employee e Atualiza

        command.setMessageType("UpdateEmployeeCommand");

        var sendingMessage = objectMapper.writeValueAsString(command);

        rabbitTemplate.convertAndSend("EmployeeRequestChannel", sendingMessage);

    }

    public void handleEmpUpdatedEvent(EmpUpdatedEvent event) throws JsonProcessingException {

        if (event.getProceedSaga()) {

            UpEmpUserCommand command = UpEmpUserCommand.builder()
                    .userId(this.userID)
                    .name(event.getName())
                    .email(event.getEmail())
                    .messageType("UpEmpUserCommand")
                    .build();

            var sendingMessage = objectMapper.writeValueAsString(command);

            rabbitTemplate.convertAndSend("AuthRequestChannel", sendingMessage);

        } else {
            System.out.println("Update employee saga completed for user ID: " + this.userID);
        }
    }

    public void handleEmpUserUpEvent(UpEmpUserEvent event) {
        System.out.println("Update employee saga completed for user ID: " + event.getUserId());
    }
}
