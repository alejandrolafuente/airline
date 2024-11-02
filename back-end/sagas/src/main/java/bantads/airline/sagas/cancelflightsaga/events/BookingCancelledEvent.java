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
public class BookingCancelledEvent {
    private String userId;
    private BigDecimal refundMoney;
    private Integer refundMiles;
    private String messageType;
}