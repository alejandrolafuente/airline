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
public class R03ResDTO {
    private UUID flightId;
    private String flightCode;
    private String flightDate;
    private String flighTime;
    private String departureAirport; // ex g. "CWB", "GRU"
    private String arrivalAirport; // ex g. "CWB", "GRU"
}
