package bantads.airline.sagas.bookingsaga;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.dto.request.BookingQueryDTO;
import bantads.airline.sagas.bookingsaga.commands.CreateBookingCommand;
import bantads.airline.sagas.bookingsaga.commands.UpdateMilesCommand;
import bantads.airline.sagas.bookingsaga.commands.UpdateSeatsCommand;
import bantads.airline.sagas.bookingsaga.events.BookingCreatedEvent;
import bantads.airline.sagas.bookingsaga.events.MilesUpdatedEvent;
import bantads.airline.sagas.bookingsaga.events.SeatsUpdatedEvent;

@Component
public class BookingSAGA {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingQueryDTO bookingQueryDTO;

    private UUID transactionId;

    public void handleRequest(BookingQueryDTO bookingQueryDTO) throws JsonProcessingException {

        this.bookingQueryDTO = bookingQueryDTO;

        UpdateMilesCommand updateMilesCommand = UpdateMilesCommand.builder()
                .usedMiles(bookingQueryDTO.getUsedMiles())
                .moneyValue(bookingQueryDTO.getMoneyValue())
                .userId(bookingQueryDTO.getUserId())
                .messageType("UpdateMilesCommand")
                .build();

        var message = objectMapper.writeValueAsString(updateMilesCommand);

        rabbitTemplate.convertAndSend("ClientRequestChannel", message);

    }

    public void handleMilesUpdatedEvent(MilesUpdatedEvent milesUpdatedEvent) throws JsonProcessingException {

        this.transactionId = milesUpdatedEvent.getTransactionId();

        UpdateSeatsCommand updateSeatsCommand = UpdateSeatsCommand.builder()
                .flightId(this.bookingQueryDTO.getFlightId())
                .totalSeats(this.bookingQueryDTO.getTotalSeats())
                .messageType("UpdateSeatsCommand")
                .build();

        var message = objectMapper.writeValueAsString(updateSeatsCommand);

        rabbitTemplate.convertAndSend("FlightRequestChannel", message);

    }

    public void handleSeatsUpdatedEvent(SeatsUpdatedEvent seatsUpdatedEvent) throws JsonProcessingException {

        CreateBookingCommand createBookingCommand = CreateBookingCommand.builder()
                .flightId(this.bookingQueryDTO.getFlightId())
                .flightCode(seatsUpdatedEvent.getFlightCode())
                .moneyValue(this.bookingQueryDTO.getMoneyValue())
                .usedMiles(this.bookingQueryDTO.getUsedMiles())
                .totalSeats(this.bookingQueryDTO.getTotalSeats()) // mudar para vir do servico voo
                .userId(this.bookingQueryDTO.getUserId())
                .transactionId(this.transactionId)
                .messageType("CreateBookingCommand")
                .build();

        var message = objectMapper.writeValueAsString(createBookingCommand);

        rabbitTemplate.convertAndSend("BookingCommandRequestChannel", message);
    }

    public void handleBookingCreatedEvent(BookingCreatedEvent bookingCreatedEvent) {

        System.out.println("VEM DO SERVIÇO DE RESERVA - ESCRITA: " + bookingCreatedEvent);

    }

}
