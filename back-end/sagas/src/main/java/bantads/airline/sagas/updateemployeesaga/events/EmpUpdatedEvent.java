package bantads.airline.sagas.updateemployeesaga.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmpUpdatedEvent {
    private Boolean proceedSaga;
    private String name;
    private String email;
    private String messageType;
}
