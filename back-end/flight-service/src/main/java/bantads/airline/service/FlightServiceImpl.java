package bantads.airline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bantads.airline.dto.response.R11ResDTO;
import bantads.airline.model.Flight;
import bantads.airline.repository.AirportRepository;
import bantads.airline.repository.FlightRepository;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Override
    public List<R11ResDTO> getflights() {

        List<Flight> flightsList = flightRepository.findAll();

        return null;
    }

}
