package bantads.airline.dto.response;

import bantads.airline.model.Flight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R04ResDTO {
    private String departure;
    private String arrival;

    public R04ResDTO(Flight flight) {
        this.departure = flight.getDepartureAirport().getCode() + " - " + flight.getDepartureAirport().getCity();

        this.arrival = flight.getArrivalAirport().getCode() + " - " + flight.getArrivalAirport().getCity();

    }
}
