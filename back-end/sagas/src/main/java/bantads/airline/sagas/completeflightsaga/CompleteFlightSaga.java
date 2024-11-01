package bantads.airline.sagas.completeflightsaga;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.completeflightsaga.commands.CompFlightCommand;

@Component
public class CompleteFlightSaga {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void handleRequest(String flightId) throws JsonProcessingException {

        // 1 ir para servico de voo e atualizar o voo de "CONFIRMED" para "COMPLETED"
        CompFlightCommand compFlightCommand = CompFlightCommand.builder()
                .flightId(flightId)
                .messageType("CompFlightCommand")
                .build();

        var message = objectMapper.writeValueAsString(compFlightCommand);

        rabbitTemplate.convertAndSend("FlightRequestChannel", message);
    }

}
