package bantads.airline.sagas.commands;

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
    private UUID flightId; // 44b9e844-ea19-44ef-9a01-461e152eacd2
    private Integer totalSeats;
    private String messageType;

}
