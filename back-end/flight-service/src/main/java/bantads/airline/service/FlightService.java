package bantads.airline.service;

import java.util.List;

import bantads.airline.dto.response.R11ResDTO;
import bantads.airline.model.Airport;

public interface FlightService {
    // R11
    List<R11ResDTO> getflights();

    // api composition teste - deletar
    List<Airport> getAllAirports();
}
