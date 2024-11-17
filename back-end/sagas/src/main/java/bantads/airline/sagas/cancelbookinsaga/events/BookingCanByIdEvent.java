package bantads.airline.sagas.cancelbookinsaga.events;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingCanByIdEvent {
    private String flightCode;
    private BigDecimal moneySpent;
    private Integer milesSpent;
    private Integer numberOfSeats;
    private String userId;
    private String messageType;
}
