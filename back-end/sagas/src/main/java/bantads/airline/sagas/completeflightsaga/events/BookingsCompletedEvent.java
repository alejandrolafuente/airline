package bantads.airline.sagas.completeflightsaga.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingsCompletedEvent {
    private String flightCode;
    private String messageType;
}
