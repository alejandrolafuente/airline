package bantads.airline.sagas.bookingsaga;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.dto.BookingQueryDTO;
import bantads.airline.sagas.bookingsaga.commands.UpdateMilesCommand;
import bantads.airline.sagas.bookingsaga.events.MilesUpdatedEvent;

@Component
public class BookingSAGA {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingQueryDTO bookingQueryDTO;

    public void handleRequest(BookingQueryDTO bookingQueryDTO) throws JsonProcessingException {

        this.bookingQueryDTO = bookingQueryDTO;

        System.out.println("O QUE CHEGA DO FRONT: " + this.bookingQueryDTO);


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

    public void handleMilesUpdatedEvent(MilesUpdatedEvent milesUpdatedEvent) {

        System.out.println("O QUE FOI FEITO EM CLIENTE: " + milesUpdatedEvent);
    }

}
