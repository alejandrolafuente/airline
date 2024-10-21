package bantads.airline.sagas.bookingsaga.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MilesUpdatedEvent {
    private Integer milesBalance;
    private String messageType;
}
