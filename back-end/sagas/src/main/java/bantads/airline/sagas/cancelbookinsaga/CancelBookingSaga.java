package bantads.airline.sagas.cancelbookinsaga;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.cancelbookinsaga.commands.CancelBookingByIdCommand;
import bantads.airline.sagas.cancelbookinsaga.commands.FreeSeatsCommand;
import bantads.airline.sagas.cancelbookinsaga.commands.RefundClientCommand;
import bantads.airline.sagas.cancelbookinsaga.events.AvailableSeatsEvent;
import bantads.airline.sagas.cancelbookinsaga.events.BookingCanByIdEvent;

@Component
public class CancelBookingSaga {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private BigDecimal moneySpent;

    private Integer milesSpent;

    private String userId;

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

        this.moneySpent = event.getMoneySpent();
        this.milesSpent = event.getMilesSpent();
        this.userId = event.getUserId();

        // 2 ir para o servico de voo e liberar poltronas ocupadas
        FreeSeatsCommand command = FreeSeatsCommand.builder()
                .flightCode(event.getFlightCode())
                .numberOfSeats(event.getNumberOfSeats())
                .messageType("FreeSeatsCommand")
                .build();

        var message = objectMapper.writeValueAsString(command);

        rabbitTemplate.convertAndSend("FlightRequestChannel", message);
    }

    public void handleAvailableSeatsEvent(AvailableSeatsEvent event) throws JsonProcessingException {

        // 3 ir para o serviço de cliente e fazer a transacao de ressarcimento,
        // atualizar saldo também
        RefundClientCommand command = RefundClientCommand.builder()
                .userId(this.userId)
                .refundMoney(this.moneySpent)
                .refundMiles(this.milesSpent)
                .messageType("RefundClientCommand")
                .build();

        var message = objectMapper.writeValueAsString(command);

        rabbitTemplate.convertAndSend("ClientRequestChannel", message);
    }

}
