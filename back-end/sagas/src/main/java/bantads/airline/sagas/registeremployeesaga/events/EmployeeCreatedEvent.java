package bantads.airline.sagas.registeremployeesaga.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCreatedEvent {
    private String email;
    private String messageType;
}
