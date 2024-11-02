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
import bantads.airline.dto.response.R07ResDTO2;
import bantads.airline.dto.response.R11ResDTO;
import bantads.airline.dto.response.R15ResDTO;
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

        UUID depId = null;
        UUID arrId = null;

        // 1. pega o id dos aeroportos

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

        List<R07ResDTO1> listR07ResDTO1 = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Flight flight : flights) {

            ZonedDateTime localTimeFlight = flight.getFlightDate().withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

            R07ResDTO1 r07ResDTO1 = R07ResDTO1.builder()
                    .flightId(flight.getFlightId().toString())
                    .flightDate(localTimeFlight.format(dateFormatter))
                    .flighTime(localTimeFlight.format(timeFormatter))
                    .departure(flight.getDepartureAirport().getName())
                    .arrival(flight.getArrivalAirport().getName())
                    .build();

            listR07ResDTO1.add(r07ResDTO1);
        }

        return listR07ResDTO1;
    }

    // R07 - 2
    @Override
    public R07ResDTO2 getFlight(String flightID) {

        Flight flight = flightRepository.findById(UUID.fromString(flightID)).orElse(null);

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

    // R11
    @Override
    public List<R11ResDTO> getflights() {

        // gets the current date in UTC
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        System.out.println("current date in UTC => " + currentDate);

        // calculates the next 48 hours
        ZonedDateTime futureDate = currentDate.plusHours(48);

        // fetches the flights
        List<Flight> flightsList = flightRepository.getNext48HoursFlights(currentDate, futureDate);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        List<R11ResDTO> listR11ResDTO = new ArrayList<>();

        for (Flight flight : flightsList) {

            ZonedDateTime localtime = flight.getFlightDate().withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

            AirportDTO departureAirportDTO = new AirportDTO(flight.getDepartureAirport());

            AirportDTO arrivalAirportDTO = new AirportDTO(flight.getArrivalAirport());

            R11ResDTO dto = R11ResDTO.builder()
                    .flightId(flight.getFlightId())
                    .flightDate(localtime.format(dateFormatter))
                    .flighTime(localtime.format(timeFormatter))
                    .departureAirport(departureAirportDTO)
                    .arrivalAirport(arrivalAirportDTO)
                    .build();

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
        } while (flightRepository.getFlightByCode(flightCode) != null);

        // ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        // define flighDate
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

        System.out.println("DEVERIA COINCIDIR COM O BD => " + newFlight.getFlightDate());

        // fazer a dto de reposta, R15ResDTO:

        ZonedDateTime localTime = newFlight.getFlightDate().withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

        System.out.println("HORARIO LOCAL => " + localTime);

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
