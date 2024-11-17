package bantads.airline.sagas;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.commands.CancelFlightCommand;
import bantads.airline.sagas.commands.CompFlightCommand;
import bantads.airline.sagas.commands.FreeSeatsCommand;
import bantads.airline.sagas.commands.UpdateSeatsCommand;
import bantads.airline.sagas.events.AvailableSeatsEvent;
import bantads.airline.sagas.events.FlightCancelledEvent;
import bantads.airline.sagas.events.FlightCompletedEvent;
import bantads.airline.sagas.events.SeatsUpdatedEvent;

@Component
public class SagasHandler {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SagaService sagaService;

    @RabbitListener(queues = "FlightRequestChannel")
    public void handleMessage(String msg) throws JsonProcessingException, JsonMappingException {

        Object object = objectMapper.readValue(msg, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            String messageType = (String) map.get("messageType");

            switch (messageType) {

                case "UpdateSeatsCommand" -> {

                    UpdateSeatsCommand command = objectMapper.convertValue(map, UpdateSeatsCommand.class);

                    SeatsUpdatedEvent event = sagaService.updateSeats(command);

                    var message = objectMapper.writeValueAsString(event);

                    rabbitTemplate.convertAndSend("FlightReturnChannel", message);
                    break;
                }

                case "CompFlightCommand" -> {

                    CompFlightCommand command = objectMapper.convertValue(map, CompFlightCommand.class);

                    FlightCompletedEvent event = sagaService.completeFlight(command);

                    var message = objectMapper.writeValueAsString(event);

                    rabbitTemplate.convertAndSend("FlightReturnChannel", message);
                    break;
                }

                case "CancelFlightCommand" -> {

                    CancelFlightCommand command = objectMapper.convertValue(map, CancelFlightCommand.class);

                    FlightCancelledEvent event = sagaService.cancelFlight(command);

                    var message = objectMapper.writeValueAsString(event);

                    rabbitTemplate.convertAndSend("FlightReturnChannel", message);
                    break;
                }

                case "FreeSeatsCommand" -> {

                    FreeSeatsCommand command = objectMapper.convertValue(map, FreeSeatsCommand.class);

                    AvailableSeatsEvent event = sagaService.freeSeats(command);

                    var message = objectMapper.writeValueAsString(event);

                    rabbitTemplate.convertAndSend("FlightReturnChannel", message);

                    break;
                }

            }

        }
    }

}
