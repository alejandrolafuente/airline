package bantads.airline.dto.response;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import bantads.airline.model.Flight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R07ResDTO1 {
    private String flightId;
    private String flightDate;
    private String flighTime;
    private String departure;
    private String arrival;

    public R07ResDTO1(Flight flight) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        ZonedDateTime localTimeFlight = flight.getFlightDate().withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

        flightId = flight.getFlightId().toString();
        flightDate = localTimeFlight.format(dateFormatter);
        flighTime = localTimeFlight.format(timeFormatter);
        departure = flight.getDepartureAirport().getName();
        arrival = flight.getArrivalAirport().getName();
    }
}
