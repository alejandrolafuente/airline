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
import bantads.airline.sagas.commands.UpdateSeatsCommand;
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

                    UpdateSeatsCommand updateSeatsCommand = objectMapper.convertValue(map, UpdateSeatsCommand.class);

                    SeatsUpdatedEvent seatsUpdatedEvent = sagaService.updateSeats(updateSeatsCommand);

                    var message = objectMapper.writeValueAsString(seatsUpdatedEvent);

                    rabbitTemplate.convertAndSend("FlightReturnChannel", message);
                    break;
                }

                case "CompFlightCommand" -> {

                    CompFlightCommand compFlightCommand = objectMapper.convertValue(map, CompFlightCommand.class);

                    FlightCompletedEvent flightCompletedEvent = sagaService.completeFlight(compFlightCommand);

                    var message = objectMapper.writeValueAsString(flightCompletedEvent);

                    rabbitTemplate.convertAndSend("FlightReturnChannel", message);
                    break;
                }

                case "CancelFlightCommand" -> {

                    CancelFlightCommand cancelFlightCommand = objectMapper.convertValue(map, CancelFlightCommand.class);

                    FlightCancelledEvent flightCancelledEvent = sagaService.cancelFlight(cancelFlightCommand);

                    var message = objectMapper.writeValueAsString(flightCancelledEvent);

                    rabbitTemplate.convertAndSend("FlightReturnChannel", message);
                    break;
                }

            }

        }
    }

}
