package bantads.airline.dto.response;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import bantads.airline.model.Flight;
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

    public R03ResDTO(Flight flight) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        ZonedDateTime localTime = flight.getFlightDate().withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

        this.flightId = flight.getFlightId();
        this.flightCode = flight.getCode();
        this.flightDate = localTime.format(dateFormatter);
        this.flighTime = localTime.format(timeFormatter);
        this.departureAirport = flight.getDepartureAirport().getCode();
        this.arrivalAirport = flight.getArrivalAirport().getCode();
    }
}
