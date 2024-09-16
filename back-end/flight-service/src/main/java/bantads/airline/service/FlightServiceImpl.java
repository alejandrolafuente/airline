package bantads.airline.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bantads.airline.dto.response.R11ResDTO;
import bantads.airline.model.Flight;
import bantads.airline.repository.FlightRepository;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Override
    public List<R11ResDTO> getflights() {

        // Gets the current date in UTC
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        // Calculates the next 48 hours
        ZonedDateTime futureDate = currentDate.plusHours(48);

        // Fetches the flights
        List<Flight> flightsList = flightRepository.getNext48HoursFlights(currentDate, futureDate);

        // Defines the São Paulo time zone
        ZoneId saoPauloZoneId = ZoneId.of("America/Sao_Paulo");


        // Adjusts the flight dates to São Paulo time zone
        flightsList.forEach(flight -> {
            ZonedDateTime utcDateTime = flight.getFlightDate();
            // Ensure utcDateTime is in UTC before converting
            if (utcDateTime.getZone().equals(ZoneId.of("UTC"))) {
                ZonedDateTime localDateTime = utcDateTime.withZoneSameInstant(saoPauloZoneId);
                flight.setFlightDate(localDateTime);
            } else {
                System.out.println("Unexpected time zone for flight date: " + utcDateTime.getZone());
            }
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Prints the flight dates in São Paulo time zone
        flightsList.forEach(
                flight -> System.out.println("Flight Date Local: " + flight.getFlightDate().format(formatter)));

        return null; // Kept as null as requested
    }

}
