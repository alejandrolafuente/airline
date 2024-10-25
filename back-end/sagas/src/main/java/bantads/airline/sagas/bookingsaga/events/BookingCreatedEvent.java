package bantads.airline.sagas.bookingsaga.events;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingCreatedEvent {
    private String bookingStatus;
    private String bookingCode;
    private ZonedDateTime bookingDate;
    private String messageType;
}
