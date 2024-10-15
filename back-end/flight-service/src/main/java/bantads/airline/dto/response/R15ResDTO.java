package bantads.airline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R15ResDTO {
    private String departureAirport; // "code - name"
    private String arrivalAirport; // "code - name"
    private String flighDate;
    private String flighTime;
    private String flighCode;
}
