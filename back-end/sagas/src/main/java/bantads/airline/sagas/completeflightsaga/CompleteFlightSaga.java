package bantads.airline.sagas.completeflightsaga;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.completeflightsaga.commands.CompFlightCommand;
import bantads.airline.sagas.completeflightsaga.commands.CompleteBookingsCommand;
import bantads.airline.sagas.completeflightsaga.events.BookingsCompletedEvent;
import bantads.airline.sagas.completeflightsaga.events.FlightCompletedEvent;

@Component
public class CompleteFlightSaga {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void handleRequest(UUID flightId) throws JsonProcessingException {

        // 1. ir para servico de voo e atualizar o Status de voo de "CONFIRMED" para
        // "COMPLETED"
        CompFlightCommand compFlightCommand = CompFlightCommand.builder()
                .flightId(flightId)
                .messageType("CompFlightCommand")
                .build();

        var message = objectMapper.writeValueAsString(compFlightCommand);

        rabbitTemplate.convertAndSend("FlightRequestChannel", message);
    }

    public void handleFlightCompletedEvent(FlightCompletedEvent flightCompletedEvent) throws JsonProcessingException {

        // 2. ir para servico de reserva (comandos) e atualizar o Status das Reservas
        // vinculadas de 'BOARDED' para "COMPLETED"
        // Se status = 'BOOKED' OU 'CHECK-IN', vai para 'NOT COMPLETED'
        CompleteBookingsCommand completeBookingCommand = CompleteBookingsCommand.builder()
                .flightCode(flightCompletedEvent.getFlightCode())
                .messageType("CompleteBookingsCommand")
                .build();

        var message = objectMapper.writeValueAsString(completeBookingCommand);

        rabbitTemplate.convertAndSend("BookingCommandRequestChannel", message);
    }

    public void handleBookingsCompletedEvent(BookingsCompletedEvent event) throws JsonProcessingException {

        System.out.println("RESERVAS COMPLETADAS PARA VOO ==> " + event.getFlightCode());
    }

}
