package bantads.airline.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bantads.airline.dto.response.AirportDTO;
import bantads.airline.dto.response.R03ResDTO;
import bantads.airline.dto.response.R11ResDTO;
import bantads.airline.model.Flight;
import bantads.airline.repository.FlightRepository;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository flightRepository;

    // @Autowired
    // private AirportRepository airportRepository;

    // R03
    @Override
    public List<R03ResDTO> getBookedFlights(List<String> flightCodes) {

        List<Flight> flightsList = new ArrayList<>();

        for (String flightCode : flightCodes) {

            flightsList.add(flightRepository.getFlightByCode(flightCode));
        }

        flightsList.sort(Comparator.comparing(Flight::getFlightDate));

        ZoneId saoPauloZoneId = ZoneId.of("America/Sao_Paulo");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<R03ResDTO> listR03ResDTO = new ArrayList<>();

        for (Flight flight : flightsList) {

            ZonedDateTime utcDateTime = flight.getFlightDate();

            if (utcDateTime.getZone().equals(ZoneId.of("UTC"))) {

                ZonedDateTime localDateTime = utcDateTime.withZoneSameInstant(saoPauloZoneId);

                flight.setFlightDate(localDateTime);
            }

            R03ResDTO dto = R03ResDTO.builder()
                    .flightId(flight.getFlightId())
                    .flightCode(flight.getCode())
                    .flightDate(flight.getFlightDate().format(dateFormatter))
                    .flighTime(flight.getFlightDate().format(timeFormatter))
                    .departureAirport(flight.getDepartureAirport().getCode())
                    .arrivalAirport(flight.getArrivalAirport().getCode())
                    .build();

            listR03ResDTO.add(dto);
        }

        return listR03ResDTO;
    }

    // R11
    @Override
    public List<R11ResDTO> getflights() {

        // gets the current date in UTC
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        // calculates the next 48 hours
        ZonedDateTime futureDate = currentDate.plusHours(48);

        // fetches the flights
        List<Flight> flightsList = flightRepository.getNext48HoursFlights(currentDate, futureDate);

        // defines the São Paulo time zone
        ZoneId saoPauloZoneId = ZoneId.of("America/Sao_Paulo");

        // adjusts the flight dates to São Paulo time zone
        flightsList.forEach(flight -> {

            ZonedDateTime utcDateTime = flight.getFlightDate();

            // ensure utcDateTime is in UTC before converting
            if (utcDateTime.getZone().equals(ZoneId.of("UTC"))) {

                ZonedDateTime localDateTime = utcDateTime.withZoneSameInstant(saoPauloZoneId);

                flight.setFlightDate(localDateTime);

            }
            // else {
            // System.out.println("Unexpected time zone for flight date: " +
            // utcDateTime.getZone());
            // }
        });

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        List<R11ResDTO> listR11ResDTO = new ArrayList<>();

        for (Flight flight : flightsList) {

            AirportDTO departureAirportDTO = new AirportDTO(flight.getDepartureAirport());

            AirportDTO arrivalAirportDTO = new AirportDTO(flight.getArrivalAirport());

            R11ResDTO dto = R11ResDTO.builder()
                    .flightId(flight.getFlightId())
                    .flightDate(flight.getFlightDate().format(dateFormatter))
                    .flighTime(flight.getFlightDate().format(timeFormatter))
                    .departureAirport(departureAirportDTO)
                    .arrivalAirport(arrivalAirportDTO)
                    .build();

            listR11ResDTO.add(dto);
        }

        return listR11ResDTO;
    }

}
