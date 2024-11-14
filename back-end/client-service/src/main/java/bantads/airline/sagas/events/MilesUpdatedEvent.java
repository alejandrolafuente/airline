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
public class MilesUpdatedEvent {
    private UUID transactionId;
    private Integer milesBalance;
    private String messageType;
}
