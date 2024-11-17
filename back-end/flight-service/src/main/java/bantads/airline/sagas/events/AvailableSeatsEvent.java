package bantads.airline.sagas.events;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableSeatsEvent {
    private String flightCode;
    private UUID flightId;
    private Integer occupiedSeats;
    private String messageType;
}
