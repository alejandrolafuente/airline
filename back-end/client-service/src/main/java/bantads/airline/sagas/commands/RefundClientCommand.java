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
public class RefundClientCommand {
    private String saga;
    private String userId;
    private BigDecimal refundMoney;
    private Integer refundMiles;
    private String messageType;
}
