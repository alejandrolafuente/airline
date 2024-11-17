package bantads.airline.service;

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
import bantads.airline.dto.response.R03ResDTO;
import bantads.airline.dto.response.R04ResDTO;
import bantads.airline.dto.response.R07ResDTO1;
import bantads.airline.dto.response.R07ResDTO2;
import bantads.airline.dto.response.R09ResDTO;
import bantads.airline.dto.response.R11ResDTO;
import bantads.airline.dto.response.R15ResDTO;
import bantads.airline.exceptions.FlightNotFoundException;
import bantads.airline.exceptions.FlightsListNotFoundException;
import bantads.airline.exceptions.MissingFlightException;
import bantads.airline.exceptions.NewFlightException;
import bantads.airline.exceptions.NoFlightNotFoundException;
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

    // R03 - 3
    @Override
    public List<R03ResDTO> getClientFlights(List<String> flightCodes) {

        List<Flight> flightsList = new ArrayList<>();

        for (String flightCode : flightCodes) {

            flightRepository.getFlightByCode(flightCode).ifPresent(flightsList::add);
        }

        if (flightsList.isEmpty()) {
            throw new NoFlightNotFoundException("No flights found for the codes provided.");
        }

        flightsList.sort(Comparator.comparing(Flight::getFlightDate));

        // **************************************************************

        List<R03ResDTO> listR03ResDTO = new ArrayList<>();

        for (Flight flight : flightsList) {

            R03ResDTO dto = new R03ResDTO(flight);

            listR03ResDTO.add(dto);
        }

        return listR03ResDTO;
    }

    // R04
    @Override
    public R04ResDTO getBookingFlight(String flightCode) {

        Flight flight = flightRepository.getFlightByCode(flightCode)
                .orElseThrow(() -> new FlightNotFoundException("Flight with code " + flightCode + " not found."));
        return new R04ResDTO(flight);
    }

    // R07 - 1
    @Override
    public List<R07ResDTO1> getClientRequestflights(R07QueDTO1 dto) {

        UUID depId = null;
        UUID arrId = null;

        if ((dto.getDepAirportCode() != null && dto.getArrAirportCode() == null)
                || (dto.getDepAirportCode() == null && dto.getArrAirportCode() != null)) {

            if (dto.getDepAirportCode() == null) {
                throw new MissingFlightException("Departure Airport not specified");
            } else if (dto.getArrAirportCode() == null) {
                throw new MissingFlightException("Arrival Airport not specified");
            }

        }

        // 1. pega o id dos aeroportos usando o codigo do mesmo; se null nao faz

        if (dto.getDepAirportCode() != null) {

            depId = airportRepository.getAirportByCode(dto.getDepAirportCode()).getAirportId();

        }

        if (dto.getArrAirportCode() != null) {
            arrId = airportRepository.getAirportByCode(dto.getArrAirportCode()).getAirportId();

        }

        // Hora local em UTC!
        ZonedDateTime localTime = ZonedDateTime.now(ZoneId.of("UTC"));

        // 2. faz busca na tabela,

        List<Flight> flights;

        // Se ambos os aeroportos forem nulos, retorna todos os voos
        if (depId == null && arrId == null) {
            flights = flightRepository.getAllFlightsAfterDate(localTime);
        } else {
            // Mantém a lógica original se os aeroportos forem fornecidos
            flights = flightRepository.getClientRequestflights(depId, arrId, localTime);
        }

        List<R07ResDTO1> dtoList = new ArrayList<>();

        for (Flight flight : flights) {

            R07ResDTO1 resDto = new R07ResDTO1(flight);

            dtoList.add(resDto);
        }

        return dtoList;
    }

    // R07 - 2
    @Override
    public R07ResDTO2 getFlight(String flightID) {

        Flight flight = flightRepository.findById(UUID.fromString(flightID)).orElseThrow(

                () -> new FlightNotFoundException("Flight nor found for ID: " + flightID));

        if (flight != null) {

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            ZonedDateTime localTimeFlight = flight.getFlightDate().withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

            R07ResDTO2 returnDTO = R07ResDTO2.builder()
                    .flightId(flight.getFlightId().toString())
                    .departure(flight.getDepartureAirport().getCity() + " - " + flight.getDepartureAirport().getName())
                    .arrival(flight.getArrivalAirport().getCity() + " - " + flight.getArrivalAirport().getName())
                    .flightDate(localTimeFlight.format(dateFormatter))
                    .flighTime(localTimeFlight.format(timeFormatter))
                    .seatPrice(flight.getFlightPrice().toString())
                    .totalSeats(flight.getTotalSeats().toString())
                    .freeSeats(String.valueOf(flight.getTotalSeats() - flight.getOccupiedSeats()))
                    .build();

            return returnDTO;
        }

        return null;
    }

    // R09 - 2
    @Override
    public R09ResDTO searchFlight(String flightCode) {

        Flight flight = flightRepository.getFlightByCode(flightCode)
                .orElseThrow(() -> new FlightNotFoundException("Flight with code " + flightCode + " not found."));

        return new R09ResDTO(flight);
    }

    // R11
    @Override
    public List<R11ResDTO> getflights() {

        // gets the current date in UTC
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        // calculates the next 48 hours
        ZonedDateTime futureDate = currentDate.plusHours(48);

        List<Flight> flightsList;

        try {
            flightsList = flightRepository.getNext48HoursFlights(currentDate, futureDate);
        } catch (Exception e) {
            throw new FlightsListNotFoundException("Cannot find flights, DB is down");
        }

        List<R11ResDTO> listR11ResDTO = new ArrayList<>();

        for (Flight flight : flightsList) {

            R11ResDTO dto = new R11ResDTO(flight);
            listR11ResDTO.add(dto);
        }

        return listR11ResDTO;
    }

    // R15 - insert flight
    @Override
    public R15ResDTO insertFlight(R15QueDTO r15QueDTO) {

        // generate flight code
        String flightCode;

        do {
            flightCode = generateRandomCode();
        } while (flightRepository.getFlightByCode(flightCode).isPresent());

        // define flighDate in UTC
        ZonedDateTime flightDate = convertToZonedDateTime(r15QueDTO.getFlighDate(),
                r15QueDTO.getFlighTime());

        Airport departureAirport = airportRepository.getAirportByCode(r15QueDTO.getDepartureAirport());
        Airport arrivalAirport = airportRepository.getAirportByCode(r15QueDTO.getArrivalAirport());

        Flight newFlight = Flight.builder()
                .code(flightCode)
                .flightDate(flightDate)
                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)
                .flightPrice(r15QueDTO.getFlightPrice())
                .totalSeats(r15QueDTO.getTotalSeats())
                .occupiedSeats(0)
                .flightStatus("CONFIRMED")
                .build();

        try {
            newFlight = flightRepository.save(newFlight);
        } catch (Exception e) {
            throw new NewFlightException("Cannot create flight");
        }

        return new R15ResDTO(newFlight);

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

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Converter as strings para LocalDate e LocalTime
        LocalDate date = LocalDate.parse(flightDate, dateFormatter);
        LocalTime time = LocalTime.parse(flightTime);

        ZonedDateTime localDateTime = ZonedDateTime.of(date, time, ZoneId.of("America/Sao_Paulo"));

        // Converter para UTC
        return localDateTime.withZoneSameInstant(ZoneOffset.UTC);
    }
}
