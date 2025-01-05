package bantads.airline.sagas.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpEmpUserCommand {
    private String userId;
    private String name;
    private String email;
    private String messageType;
}
