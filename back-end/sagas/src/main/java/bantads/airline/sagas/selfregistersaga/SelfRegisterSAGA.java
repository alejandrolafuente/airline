package bantads.airline.sagas.selfregistersaga;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.dto.request.SelfRegDTO;
import bantads.airline.sagas.selfregistersaga.commands.CreateClientCommand;
import bantads.airline.sagas.selfregistersaga.commands.CreateUserCommand;
import bantads.airline.sagas.selfregistersaga.emailservice.EmailService;
import bantads.airline.sagas.selfregistersaga.events.ClientCreatedEvent;
import bantads.airline.sagas.selfregistersaga.events.UserCreatedEvent;

// ORQUESTRATOR
@Component
public class SelfRegisterSAGA {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private SelfRegDTO selfRegDTO;

    private String userPswd;

    @Autowired
    private EmailService emailService;

    public void handleRequest(SelfRegDTO selfRegDTO) throws JsonProcessingException {

        this.selfRegDTO = selfRegDTO;

        CreateUserCommand createUserCommand = CreateUserCommand.builder()
                .name(this.selfRegDTO.getName())
                .email(this.selfRegDTO.getEmail())
                .messageType("CreateUserCommand")
                .build();
                
        var sendingMessage = objectMapper.writeValueAsString(createUserCommand);

        rabbitTemplate.convertAndSend("AuthRequestChannel", sendingMessage);
    }

    public void handleUserCreatedEvent(UserCreatedEvent userCreatedEvent)
            throws JsonMappingException, JsonProcessingException {

        this.userPswd = userCreatedEvent.getUserPswd();

        System.out.println("GENERATED PASSWORD, SEND BY EMAIL => " + this.userPswd);

        CreateClientCommand newClient = CreateClientCommand.builder()
                .userId(userCreatedEvent.getUserId())
                .cpf(this.selfRegDTO.getCpf())
                .name(this.selfRegDTO.getName())
                .email(this.selfRegDTO.getEmail())
                .addressType(this.selfRegDTO.getAddressType())
                .number(this.selfRegDTO.getNumber())
                .complement(this.selfRegDTO.getComplement())
                .cep(this.selfRegDTO.getCep())
                .city(this.selfRegDTO.getCity())
                .state(this.selfRegDTO.getState())
                .messageType("CreateClientCommand")
                .build();

        var sendingMessage = objectMapper.writeValueAsString(newClient);

        rabbitTemplate.convertAndSend("ClientRequestChannel", sendingMessage);

    }

    public void handleClientCreatedEvent(ClientCreatedEvent clientCreatedEvent) {

        String subject = "AIRLINE: YOU HAVE BEEN REGISTERED";

        String message = "Your airline system password is " + this.userPswd;

        this.emailService.sendApproveEmail(this.selfRegDTO.getEmail(), subject, message);
    }
}
