package bantads.airline.sagas.listeners;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.bookingsaga.BookingSAGA;
import bantads.airline.sagas.bookingsaga.events.BookingCreatedEvent;
import bantads.airline.sagas.cancelbookinsaga.CancelBookingSaga;
import bantads.airline.sagas.cancelbookinsaga.events.BookingCanByIdEvent;
import bantads.airline.sagas.cancelflightsaga.CancelFlightSaga;
import bantads.airline.sagas.cancelflightsaga.events.BookingCancelledEvent;
import bantads.airline.sagas.completeflightsaga.CompleteFlightSaga;
import bantads.airline.sagas.completeflightsaga.events.BookingsCompletedEvent;

@Component
public class BookCommandReturnChannelListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingSAGA bookingSAGA;

    @Autowired
    private CancelFlightSaga cancelFlightSaga;

    @Autowired
    private CancelBookingSaga cancelBookingSaga;

    @Autowired
    private CompleteFlightSaga completeFlightSaga;

    @RabbitListener(queues = "BookingCommandReturnChannel")
    public void handleCommandResponses(String receivedMessage) throws JsonMappingException, JsonProcessingException {

        Object object = objectMapper.readValue(receivedMessage, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            String messageType = (String) map.get("messageType");

            switch (messageType) {

                case "BookingCreatedEvent" -> {

                    BookingCreatedEvent event = objectMapper.convertValue(map, BookingCreatedEvent.class);

                    bookingSAGA.handleBookingCreatedEvent(event);

                    break;

                }

                case "BookingCancelledEvent" -> {

                    BookingCancelledEvent event = objectMapper.convertValue(map, BookingCancelledEvent.class);

                    cancelFlightSaga.handleBookingCancelledEvent(event);

                    break;

                }

                case "BookingCanByIdEvent" -> {

                    BookingCanByIdEvent event = objectMapper.convertValue(map, BookingCanByIdEvent.class);

                    cancelBookingSaga.handleBookingCanByIdEvent(event);
                }

                case "BookingsCompletedEvent" -> {

                    BookingsCompletedEvent event = objectMapper.convertValue(map, BookingsCompletedEvent.class);

                    completeFlightSaga.handleBookingsCompletedEvent(event);
                }

                default -> {
                    // you can handle unknown message types or log an error
                }

            }
        }
    }

}
