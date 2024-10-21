package bantads.airline.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingQueryDTO {
    private String flightId;
    private BigDecimal moneyValue;
    private Integer usedMiles;
    private Integer totalSeats;
    private String userId;
}
