package bantads.airline.sagas.cancelflightsaga;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.cancelflightsaga.commands.CancelBookingCommand;
import bantads.airline.sagas.cancelflightsaga.commands.CancelFlightCommand;
import bantads.airline.sagas.cancelflightsaga.events.FlightCancelledEvent;

@Component
public class CancelFlightSaga {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void handleRequest(String flightId) throws JsonProcessingException {

        // 1 ir para servico de voo e atualizar o voo de "CONFIRMED" para "COMPLETED"
        CancelFlightCommand cancelFlightCommand = CancelFlightCommand.builder()
                .flightId(flightId)
                .messageType("CancelFlightCommand")
                .build();

        var message = objectMapper.writeValueAsString(cancelFlightCommand);

        rabbitTemplate.convertAndSend("FlightRequestChannel", message);

    }

    public void handleFlightCancelledEvent(FlightCancelledEvent flightCancelledEvent) throws JsonProcessingException {

        // 2 ir para o serviço de reserva e mudar o Estado, laço aqui
        CancelBookingCommand cancelBookingCommand = CancelBookingCommand.builder()
                .flightCode(flightCancelledEvent.getFlightCode())
                .messageType("CancelBookingCommand")
                .build();

        var message = objectMapper.writeValueAsString(cancelBookingCommand);

        rabbitTemplate.convertAndSend("BookingCommandRequestChannel", message);

    }

}
