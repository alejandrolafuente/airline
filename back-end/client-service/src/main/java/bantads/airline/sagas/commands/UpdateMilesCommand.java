package bantads.airline.sagas.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMilesCommand {
    private Integer usedMiles;
    private String userId;
    private String messageType;
}
