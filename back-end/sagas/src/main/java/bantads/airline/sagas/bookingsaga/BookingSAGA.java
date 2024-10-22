package bantads.airline.sagas.bookingsaga;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.dto.BookingQueryDTO;
import bantads.airline.sagas.bookingsaga.commands.UpdateMilesCommand;
import bantads.airline.sagas.bookingsaga.commands.UpdateSeatsCommand;
import bantads.airline.sagas.bookingsaga.events.MilesUpdatedEvent;
import bantads.airline.sagas.bookingsaga.events.SeatsUpdatedEvent;

@Component
public class BookingSAGA {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingQueryDTO bookingQueryDTO;

    public void handleRequest(BookingQueryDTO bookingQueryDTO) throws JsonProcessingException {

        this.bookingQueryDTO = bookingQueryDTO;

        // 1. ir no cliente e atualizar o saldo. se usedMiles = 0 ou null
        // nao precisa atualizar o cliente
        if (bookingQueryDTO.getUsedMiles() != 0 && bookingQueryDTO.getUsedMiles() != null) {

            UpdateMilesCommand updateMilesCommand = UpdateMilesCommand.builder()
                    .usedMiles(bookingQueryDTO.getUsedMiles())
                    .userId(bookingQueryDTO.getUserId())
                    .messageType("UpdateMilesCommand")
                    .build();

            var message = objectMapper.writeValueAsString(updateMilesCommand);

            rabbitTemplate.convertAndSend("ClientRequestChannel", message);
        }
    }

    public void handleMilesUpdatedEvent(MilesUpdatedEvent milesUpdatedEvent) throws JsonProcessingException {

        UpdateSeatsCommand updateSeatsCommand = UpdateSeatsCommand.builder()
                .flightId(UUID.fromString(this.bookingQueryDTO.getUserId()))
                .totalSeats(this.bookingQueryDTO.getTotalSeats())
                .messageType("UpdateSeatsCommand")
                .build();

        var message = objectMapper.writeValueAsString(updateSeatsCommand);

        rabbitTemplate.convertAndSend("FlightRequestChannel", message);

    }

    public void handleSeatsUpdatedEvent(SeatsUpdatedEvent seatsUpdatedEvent) {
        System.out.println("ALTERAÇÕES EM SERVIÇO DE VOO: " + seatsUpdatedEvent);
    }

}
