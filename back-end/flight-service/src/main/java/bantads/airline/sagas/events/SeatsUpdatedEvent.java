package bantads.airline.sagas.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatsUpdatedEvent {
    private String flightCode;
    private Integer freeSeats;
    private String messageType;
}
