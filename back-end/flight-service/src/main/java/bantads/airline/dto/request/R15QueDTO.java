package bantads.airline.dto.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R15QueDTO {
    private String flighDate;
    private String flighTime;
    private String departureAirport; // code
    private String arrivalAirport; // code
    private BigDecimal flightPrice;
    private Integer totalSeats;
}
