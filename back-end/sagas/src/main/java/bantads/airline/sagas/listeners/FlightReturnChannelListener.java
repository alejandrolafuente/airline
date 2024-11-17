package bantads.airline.sagas.listeners;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.bookingsaga.BookingSAGA;
import bantads.airline.sagas.bookingsaga.events.SeatsUpdatedEvent;
import bantads.airline.sagas.cancelbookinsaga.CancelBookingSaga;
import bantads.airline.sagas.cancelbookinsaga.events.AvailableSeatsEvent;
import bantads.airline.sagas.cancelflightsaga.CancelFlightSaga;
import bantads.airline.sagas.cancelflightsaga.events.FlightCancelledEvent;
import bantads.airline.sagas.completeflightsaga.CompleteFlightSaga;
import bantads.airline.sagas.completeflightsaga.events.FlightCompletedEvent;

@Component
public class FlightReturnChannelListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingSAGA bookingSAGA;

    @Autowired
    private CompleteFlightSaga completeFlightSaga;

    @Autowired
    private CancelFlightSaga cancelFlightSaga;

    @Autowired
    private CancelBookingSaga cancelBookingSaga;

    @RabbitListener(queues = "FlightReturnChannel")
    public void handleFlightResponses(String receivedMessage) throws JsonMappingException, JsonProcessingException {

        Object object = objectMapper.readValue(receivedMessage, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            String messageType = (String) map.get("messageType");

            switch (messageType) {

                case "SeatsUpdatedEvent" -> {

                    SeatsUpdatedEvent event = objectMapper.convertValue(map, SeatsUpdatedEvent.class);

                    bookingSAGA.handleSeatsUpdatedEvent(event);

                    break;
                }

                case "FlightCompletedEvent" -> {

                    FlightCompletedEvent event = objectMapper.convertValue(map,
                            FlightCompletedEvent.class);

                    completeFlightSaga.handleFlightCompletedEvent(event);

                    break;
                }

                case "FlightCancelledEvent" -> {

                    FlightCancelledEvent event = objectMapper.convertValue(map, FlightCancelledEvent.class);

                    cancelFlightSaga.handleFlightCancelledEvent(event);

                    break;
                }

                case "AvailableSeatsEvent" -> {

                    AvailableSeatsEvent event = objectMapper.convertValue(map, AvailableSeatsEvent.class);

                    cancelBookingSaga.handleAvailableSeatsEvent(event);

                    break;
                }
            }

        }

    }

}
