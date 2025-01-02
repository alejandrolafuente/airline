package bantads.airline.sagas.registeremployeesaga.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmpUserCreatedEvent {
    private String userId;
    private String userPswd;
    private String messageType;
}
