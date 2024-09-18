package bantads.airline.dto.response;

import java.util.UUID;

import bantads.airline.model.Airport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportDTO {
    private UUID airportId;
    private String code;
    private String name;
    private String city;
    private String state;

    public AirportDTO(Airport airport) {
        this.airportId = airport.getAirportId();
        this.code = airport.getCode();
        this.name = airport.getName();
        this.city = airport.getCity();
        this.state = airport.getState();
    }
}
