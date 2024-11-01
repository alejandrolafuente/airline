package bantads.airline.sagas.completeflightsaga.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompFlightCommand {
    private String flightId;
    private String messageType;
}
