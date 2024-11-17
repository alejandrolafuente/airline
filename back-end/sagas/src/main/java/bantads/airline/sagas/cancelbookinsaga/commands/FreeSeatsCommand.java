package bantads.airline.sagas.cancelbookinsaga.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreeSeatsCommand {
    private String flightCode;
    private Integer numberOfSeats;
    private String messageType;
}
