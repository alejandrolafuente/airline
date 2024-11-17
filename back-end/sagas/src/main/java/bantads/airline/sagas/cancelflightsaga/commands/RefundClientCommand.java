package bantads.airline.sagas.cancelflightsaga.commands;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;

import bantads.airline.sagas.cancelflightsaga.events.BookingCancelledEvent;
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

    public RefundClientCommand(BookingCancelledEvent bookingCancelledEvent) {
        saga = "CancelFlightSaga";
        BeanUtils.copyProperties(bookingCancelledEvent, this);
    }
}
