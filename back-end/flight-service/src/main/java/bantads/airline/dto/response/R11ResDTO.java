package bantads.airline.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R11ResDTO {
    private UUID flightId;
    private String flightDate;
    private String flighTime;
    private AirportDTO departureAirport;
    private AirportDTO arrivalAirport;
}
