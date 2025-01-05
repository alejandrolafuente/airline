package bantads.airline.sagas.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpEmpUserEvent {
    private String userId;
    private String messageType;
}
