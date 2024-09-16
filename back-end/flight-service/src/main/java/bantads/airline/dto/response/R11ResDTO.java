package bantads.airline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R11ResDTO {
    private String flightDate;
    private String flighTime;
    private AirportDTO departureAirport;
    private AirportDTO arrivalAirport;
}
