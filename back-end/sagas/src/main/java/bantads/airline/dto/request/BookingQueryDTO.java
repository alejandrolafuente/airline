package bantads.airline.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingQueryDTO {
    private UUID flightId;
    private BigDecimal moneyValue; // can be null
    private Integer usedMiles;    // can be null
    private Integer totalSeats;
    private String userId;
}
