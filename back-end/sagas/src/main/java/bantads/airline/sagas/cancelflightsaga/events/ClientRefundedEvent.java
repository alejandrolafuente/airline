package bantads.airline.sagas.cancelflightsaga.events;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientRefundedEvent {
    private String saga;
    private String name;
    private BigDecimal refundMoney;
    private Integer refundMiles;
    private Integer newBalance;
    private String messageType;
}
