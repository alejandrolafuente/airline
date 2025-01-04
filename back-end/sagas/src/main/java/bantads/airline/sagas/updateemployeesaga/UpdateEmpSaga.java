package bantads.airline.sagas.updateemployeesaga;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.dto.request.PutEmpDTO;
import bantads.airline.sagas.updateemployeesaga.commands.UpdateEmployeeCommand;

@Component
public class UpdateEmpSaga {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void handleRequest(PutEmpDTO dto) throws JsonProcessingException {

        UpdateEmployeeCommand command = new UpdateEmployeeCommand(dto);

        //1. vai para o serviço Employee e Atualiza

        var sendingMessage = objectMapper.writeValueAsString(command);

        rabbitTemplate.convertAndSend("EmployeeRequestChannel", sendingMessage);

    }
}
