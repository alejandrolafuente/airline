package bantads.airline.sagas.deletemployeesaga.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteUserCommand {
    private String userId;
    private String messageType;
}
