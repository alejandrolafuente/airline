package bantads.airline.sagas;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bantads.airline.model.Flight;
import bantads.airline.repository.FlightRepository;
import bantads.airline.sagas.commands.CancelFlightCommand;
import bantads.airline.sagas.commands.CompFlightCommand;
import bantads.airline.sagas.commands.UpdateSeatsCommand;
import bantads.airline.sagas.events.FlightCancelledEvent;
import bantads.airline.sagas.events.FlightCompletedEvent;
import bantads.airline.sagas.events.SeatsUpdatedEvent;

@Service
public class SagaService {

    @Autowired
    private FlightRepository flightRepository;

    public SeatsUpdatedEvent updateSeats(UpdateSeatsCommand updateSeatsCommand) {

        Flight flight = flightRepository.findById(updateSeatsCommand.getFlightId()).orElse(null);

        flight.setOccupiedSeats(flight.getOccupiedSeats() + updateSeatsCommand.getTotalSeats());

        flight = flightRepository.save(flight);

        SeatsUpdatedEvent seatsUpdatedEvent = SeatsUpdatedEvent.builder()
                .flightCode(flight.getCode())
                .freeSeats(flight.getTotalSeats() - flight.getOccupiedSeats())
                .messageType("SeatsUpdatedEvent")
                .build();

        return seatsUpdatedEvent;
    }

    // R13 - cancel flight
    public FlightCancelledEvent cancelFlight(CancelFlightCommand cancelFlightCommand) {

        Flight flight = flightRepository.findById(cancelFlightCommand.getFlightId()).orElse(null);

        flight.setFlightStatus("CANCELLED");

        flight = flightRepository.save(flight);

        FlightCancelledEvent flightCancelledEvent = FlightCancelledEvent.builder()
                .flightCode(flight.getCode())
                .messageType("FlightCancelledEvent")
                .build();

        return flightCancelledEvent;
    }

    // R14 - complete flight
    public FlightCompletedEvent completeFlight(CompFlightCommand compFlightCommand) {

        Flight flight = flightRepository.findById(UUID.fromString(compFlightCommand.getFlightId())).orElse(null);

        flight.setFlightStatus("COMPLETED");

        flight = flightRepository.save(flight);

        FlightCompletedEvent flightCompletedEvent = FlightCompletedEvent.builder()
                .flightCode(flight.getCode())
                .messageType("FlightCompletedEvent")
                .build();

        return flightCompletedEvent;
    }

}
