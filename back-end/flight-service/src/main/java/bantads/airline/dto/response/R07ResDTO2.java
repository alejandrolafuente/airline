package bantads.airline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R07ResDTO2 {
    private String flightId;
    private String departure; // ex g. "SÃO PAULO - AEROPORTO DE GUARULHOS"
    private String arrival;
    private String flightDate;
    private String flighTime;
    private String seatPrice;
    private String totalSeats;
    private String freeSeats;
}
