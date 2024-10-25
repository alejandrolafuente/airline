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
import bantads.airline.sagas.selfregistersaga.events.UserCreatedEvent;

@Component
public class BookCommandChannelListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingSAGA bookingSAGA;

    @RabbitListener(queues = "BookingCommandReturnChannel")
    public void handleCommandResponses(String receivedMessage) throws JsonMappingException, JsonProcessingException {

        Object object = objectMapper.readValue(receivedMessage, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            String messageType = (String) map.get("messageType");

            switch (messageType) {

                case "BookingCreatedEvent" -> {

                    BookingCreatedEvent bookingCreatedEvent = objectMapper.convertValue(map, BookingCreatedEvent.class);

                    bookingSAGA.handleBookingCreatedEvent(bookingCreatedEvent);

                    break;

                }

                default -> {
                    // you can handle unknown message types or log an error
                }

            }
        }
    }

}
