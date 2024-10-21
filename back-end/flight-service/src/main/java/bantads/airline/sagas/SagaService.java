package bantads.airline.sagas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bantads.airline.repository.FlightRepository;
import bantads.airline.sagas.commands.UpdateSeatsCommand;
import bantads.airline.sagas.events.SeatsUpdatedEvent;

@Service
public class SagaService {

    @Autowired
    private FlightRepository flightRepository;

    public SeatsUpdatedEvent updateSeats(UpdateSeatsCommand updateSeatsCommand) {
        return null;
    }

}
