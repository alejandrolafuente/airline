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
public class R15ResDTO {
    private String departureAirport; // "code - name"
    private String arrivalAirport; // "code - name"
    private String flighDate;
    private String flighTime;
    private String flighCode;

    public R15ResDTO(Flight newFlight) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        ZonedDateTime localTime = newFlight.getFlightDate().withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

        departureAirport = newFlight.getDepartureAirport().getCode() + " - "
                + newFlight.getDepartureAirport().getName();
        arrivalAirport = newFlight.getArrivalAirport().getCode() + " - " + newFlight.getArrivalAirport().getName();
        flighDate = localTime.format(dateFormatter);
        flighTime = localTime.format(timeFormatter);
        flighCode = newFlight.getCode();

    }
}
