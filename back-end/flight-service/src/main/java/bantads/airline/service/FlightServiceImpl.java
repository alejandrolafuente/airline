package bantads.airline.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bantads.airline.dto.request.R07QueDTO1;
import bantads.airline.dto.request.R15QueDTO;
import bantads.airline.dto.response.AirportDTO;
import bantads.airline.dto.response.R03ResDTO;
import bantads.airline.dto.response.R07ResDTO1;
import bantads.airline.dto.response.R11ResDTO;
import bantads.airline.dto.response.R15ResDTO;
import bantads.airline.model.Airport;
import bantads.airline.model.Flight;
import bantads.airline.repository.AirportRepository;
import bantads.airline.repository.FlightRepository;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

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

    // R07 - 1
    @Override
    public List<R07ResDTO1> getClientRequestflights(R07QueDTO1 dto) {

        UUID depId = airportRepository.getAirportByCode(dto.getDepAirportCode()).getAirportId();
        UUID arrId = airportRepository.getAirportByCode(dto.getArrAirportCode()).getAirportId();

        List<Flight> flights = flightRepository.getClientRequestflights(depId, arrId,
                ZonedDateTime.now(ZoneId.of("UTC")));

        System.out.println( "VOOS PESQUISADOS NO DB"  +  flights);

        return null;
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

    // R15
    @Override
    public R15ResDTO insertFlight(R15QueDTO r15QueDTO) {

        // generate flight code
        String flightCode;

        do {
            flightCode = generateRandomCode();
        } while (flightRepository.getFlightByCode(flightCode) != null);

        // define flighDate
        // ZonedDateTime convertToZonedDateTime(String flightDate, String flightTime)
        ZonedDateTime flightDate = convertToZonedDateTime(r15QueDTO.getFlighDate(), r15QueDTO.getFlighTime());

        // construindo o registro:

        Flight newFlight = Flight.builder()
                .code(flightCode)
                .flightDate(flightDate)
                .departureAirport(airportRepository.getAirportByCode(r15QueDTO.getDepartureAirport()))
                .arrivalAirport(airportRepository.getAirportByCode(r15QueDTO.getArrivalAirport()))
                .flightPrice(new BigDecimal(r15QueDTO.getFlightPrice()))
                .totalSeats(Integer.parseInt(r15QueDTO.getTotalSeats()))
                .occupiedSeats(0)
                .flightStatus("CONFIRMED")
                .build();

        newFlight = flightRepository.save(newFlight);

        // fazer a dto de reposta, R15ResDTO:

        ZonedDateTime localTime = newFlight.getFlightDate().withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        R15ResDTO dto = R15ResDTO.builder()
                .departureAirport(
                        newFlight.getDepartureAirport().getCode() + " - " + newFlight.getDepartureAirport().getName())
                .arrivalAirport(
                        newFlight.getArrivalAirport().getCode() + " - " + newFlight.getArrivalAirport().getName())
                .flighDate(localTime.format(dateFormatter))
                .flighTime(localTime.format(timeFormatter))
                .flighCode(newFlight.getCode())
                .build();

        return dto;
    }

    private String generateRandomCode() {

        String prefix = "TADS";
        String CHARACTERS = "0123456789";
        int STRING_LENGTH = 4;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(STRING_LENGTH);

        for (int i = 0; i < STRING_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return prefix + sb.toString();
    }

    public ZonedDateTime convertToZonedDateTime(String flightDate, String flightTime) {
        // Converter as strings para LocalDate e LocalTime
        LocalDate date = LocalDate.parse(flightDate);
        LocalTime time = LocalTime.parse(flightTime);

        // Combinar LocalDate e LocalTime em ZonedDateTime com fuso horário
        // America/Sao_Paulo
        ZonedDateTime zonedDateTime = ZonedDateTime.of(date, time, ZoneId.of("America/Sao_Paulo"));

        // Converter para UTC antes de armazenar
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
    }

}
