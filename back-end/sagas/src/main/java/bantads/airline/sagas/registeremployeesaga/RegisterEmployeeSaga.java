package bantads.airline.sagas.registeremployeesaga;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.dto.request.NewEmployeeDTO;
import bantads.airline.sagas.registeremployeesaga.commands.CreaEmpUserCommand;
import bantads.airline.sagas.registeremployeesaga.commands.CreateEmployeeCommand;
import bantads.airline.sagas.registeremployeesaga.events.EmpUserCreatedEvent;
import bantads.airline.sagas.registeremployeesaga.events.EmployeeCreatedEvent;
import bantads.airline.sagas.selfregistersaga.emailservice.EmailService;

@Component
public class RegisterEmployeeSaga {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private NewEmployeeDTO newEmployeeDTO;

    private String userPassword;

    @Autowired
    private EmailService emailService;

    public void handleRequest(NewEmployeeDTO newEmployeeDTO) throws JsonProcessingException {

        this.newEmployeeDTO = newEmployeeDTO;

        CreaEmpUserCommand command = CreaEmpUserCommand.builder()
                .name(newEmployeeDTO.getName())
                .email(newEmployeeDTO.getEmail())
                .messageType("CreateEmployeeUserCommand")
                .build();

        var sendingMessage = objectMapper.writeValueAsString(command);

        rabbitTemplate.convertAndSend("AuthRequestChannel", sendingMessage);

    }

    public void handleUserCreatedEvent(EmpUserCreatedEvent event) throws JsonProcessingException {

        this.userPassword = event.getUserPswd();

        CreateEmployeeCommand command = CreateEmployeeCommand.builder()
                .userID(event.getUserId())
                .name(newEmployeeDTO.getName())
                .cpf(newEmployeeDTO.getCpf())
                .email(newEmployeeDTO.getEmail())
                .phoneNumber(newEmployeeDTO.getPhoneNumber())
                .messageType("CreateEmployeeCommand")
                .build();

        var message = objectMapper.writeValueAsString(command);

        rabbitTemplate.convertAndSend("EmployeeRequestChannel", message);
    }

    public void handleEmployeeCreatedEvent(EmployeeCreatedEvent event) {

        String subject = "AIRLINE: YOU HAVE BEEN REGISTERED";

        String message = "Your airline system password is " + this.userPassword;

        this.emailService.sendApproveEmail(event.getEmail(), subject, message);
    }

}
