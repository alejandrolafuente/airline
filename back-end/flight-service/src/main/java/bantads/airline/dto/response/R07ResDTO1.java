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
public class R07ResDTO1 {
    private UUID flightId;
    private String flightDate;
    private String flighTime;
    private String departure; // ex g. "AEROPORTO DE GUARULHOS"
    private String arrival; // ex g. "AEROPORTO GALEÃO"
}
