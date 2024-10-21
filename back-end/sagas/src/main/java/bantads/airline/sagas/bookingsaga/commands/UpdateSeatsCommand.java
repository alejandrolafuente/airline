package bantads.airline.sagas.bookingsaga.commands;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSeatsCommand {
    private UUID flightId;
    private Integer totalSeats;
    private String messageType;

}
