package bantads.airline.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R07QueDTO1 {
    private String depAirportCode;
    private String arrAirportCode;
}
