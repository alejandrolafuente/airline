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
public class R09ResDTO {
    private UUID flightId;
    private String departure;
    private String arrival;
    private String flightDate;
    private String flighTime;
    private String flightStatus;

    public R09ResDTO(Flight flight) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        ZonedDateTime localTimeFlight = flight.getFlightDate().withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

        flightId = flight.getFlightId();
        departure = flight.getDepartureAirport().getName();
        arrival = flight.getArrivalAirport().getName();
        flightDate = localTimeFlight.format(dateFormatter);
        flighTime = localTimeFlight.format(timeFormatter);
        flightStatus = flight.getFlightStatus();
    }
}
