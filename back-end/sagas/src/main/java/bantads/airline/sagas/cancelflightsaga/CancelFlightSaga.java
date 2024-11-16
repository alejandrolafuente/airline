package bantads.airline.sagas.cancelflightsaga;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.cancelflightsaga.commands.CancelBookingCommand;
import bantads.airline.sagas.cancelflightsaga.commands.CancelFlightCommand;
import bantads.airline.sagas.cancelflightsaga.commands.RefundClientCommand;
import bantads.airline.sagas.cancelflightsaga.events.BookingCancelledEvent;
import bantads.airline.sagas.cancelflightsaga.events.ClientRefundedEvent;
import bantads.airline.sagas.cancelflightsaga.events.FlightCancelledEvent;

@Component
public class CancelFlightSaga {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void handleRequest(UUID flightId) throws JsonProcessingException {

        // 1 ir para servico de voo e atualizar o voo de "CONFIRMED" para "CANCELLED"
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

    public void handleBookingCancelledEvent(BookingCancelledEvent bookingCancelledEvent)
            throws JsonProcessingException {

        RefundClientCommand refundClientCommand = new RefundClientCommand(bookingCancelledEvent);

        refundClientCommand.setMessageType("RefundClientCommand");

        var message = objectMapper.writeValueAsString(refundClientCommand);

        rabbitTemplate.convertAndSend("ClientRequestChannel", message);

    }

    public void handleClientRefundedEvent(ClientRefundedEvent clientRefundedEvent) {

        System.out.println("FIM DA SAGA DE CANCELAMENTO DE VOO, CLIENTE RESSARCIDO: ");
        System.out.println("NOME: " + clientRefundedEvent.getName());
        System.out.println("DINHEIRO DEVOLVIDO: " + clientRefundedEvent.getRefundMoney());
        System.out.println("MILHAS DEVOLVIDAS: " + clientRefundedEvent.getRefundMiles());
        System.out.println("NOVO SALDO DE MILHAS: " + clientRefundedEvent.getNewBalance());
    }

}
