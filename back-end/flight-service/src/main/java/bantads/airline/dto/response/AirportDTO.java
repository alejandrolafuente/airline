package bantads.airline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportDTO {
    private String code;
    private String name;
    private String city;
    private String state;
}
