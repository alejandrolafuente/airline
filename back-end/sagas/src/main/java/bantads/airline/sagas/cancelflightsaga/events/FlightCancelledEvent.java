package bantads.airline.sagas.cancelflightsaga.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightCancelledEvent {
    private String flightCode;
    private String messageType;
}
