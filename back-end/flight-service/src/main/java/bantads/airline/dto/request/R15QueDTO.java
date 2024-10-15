package bantads.airline.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R15QueDTO {
    // codigo do voo vai ser determinado pelo sistema no formato "TADS0000"
    private String flighDate;
    private String flighTime;
    private String departureAirport; // code
    private String arrivalAirport; // code
    private String flightPrice;
    private String totalSeats;
}
