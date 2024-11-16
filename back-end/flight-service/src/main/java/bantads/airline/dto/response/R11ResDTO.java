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
public class R11ResDTO {
    private UUID flightId;
    private String flightDate;
    private String flightTime;
    private String flightStatus;
    private AirportDTO departureAirport;
    private AirportDTO arrivalAirport;

    public R11ResDTO(Flight flight) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        ZonedDateTime localtime = flight.getFlightDate().withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

        AirportDTO departureAirportDTO = new AirportDTO(flight.getDepartureAirport());

        AirportDTO arrivalAirportDTO = new AirportDTO(flight.getArrivalAirport());

        flightId = flight.getFlightId();
        flightDate = localtime.format(dateFormatter);
        flightTime = localtime.format(timeFormatter);
        flightStatus = flight.getFlightStatus();
        departureAirport = departureAirportDTO;
        arrivalAirport = arrivalAirportDTO;
    }
}
