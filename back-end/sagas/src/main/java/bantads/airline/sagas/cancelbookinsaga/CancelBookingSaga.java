package bantads.airline.sagas.cancelbookinsaga;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.cancelbookinsaga.commands.CancelBookingByIdCommand;
import bantads.airline.sagas.cancelbookinsaga.commands.FreeSeatsCommand;
import bantads.airline.sagas.cancelbookinsaga.events.BookingCanByIdEvent;

@Component
public class CancelBookingSaga {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void handleRequest(UUID bookingId) throws JsonProcessingException {

        // 1 ir para servico de reserva (escrita) e mudar status "BOOKED" -> "CANCELLED"
        CancelBookingByIdCommand cancelBookingByIdCommand = CancelBookingByIdCommand.builder()
                .bookingId(bookingId)
                .messageType("CancelBookingByIdCommand")
                .build();

        var message = objectMapper.writeValueAsString(cancelBookingByIdCommand);

        rabbitTemplate.convertAndSend("BookingCommandRequestChannel", message);

    }

    public void handleBookingCanByIdEvent(BookingCanByIdEvent event) throws JsonProcessingException {

        // 2 ir para o servico de voo e liberar poltronas ocupadas
        FreeSeatsCommand command = FreeSeatsCommand.builder()
                .flightCode(event.getFlightCode())
                .numberOfSeats(event.getNumberOfSeats())
                .messageType("FreeSeatsCommand")
                .build();

        var message = objectMapper.writeValueAsString(command);

        rabbitTemplate.convertAndSend("FlightRequestChannel", message);
    }
}
