package bantads.airline.sagas.commands;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMilesCommand {
    private Integer usedMiles; // can be null
    private BigDecimal moneyValue; // can be null
    private String userId;
    private String messageType;
}
