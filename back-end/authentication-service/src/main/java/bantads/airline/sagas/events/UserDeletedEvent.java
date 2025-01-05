package bantads.airline.sagas.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDeletedEvent {
    private String userId;
    private String userStatus;
    private String messageType;
}
