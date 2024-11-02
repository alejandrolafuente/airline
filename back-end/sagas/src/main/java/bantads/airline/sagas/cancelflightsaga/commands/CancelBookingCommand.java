package bantads.airline.sagas.cancelflightsaga.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelBookingCommand {
    private String flightCode;
    private String messageType;
}
