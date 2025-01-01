package bantads.airline.sagas.registeremployeesaga;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.dto.request.NewEmployeeDTO;
import bantads.airline.sagas.registeremployeesaga.commands.CreaEmpUserCommand;

@Component
public class RegisteremployeeSaga {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // @Autowired
    // private EmailService emailService;

    public void handleRequest(NewEmployeeDTO newEmployeeDTO) throws JsonProcessingException {

        CreaEmpUserCommand command = CreaEmpUserCommand.builder()
                .name(newEmployeeDTO.getName())
                .email(newEmployeeDTO.getEmail())
                .messageType("CreateEmployeeUserCommand")
                .build();

        var sendingMessage = objectMapper.writeValueAsString(command);

        rabbitTemplate.convertAndSend("AuthRequestChannel", sendingMessage);

    }

}
