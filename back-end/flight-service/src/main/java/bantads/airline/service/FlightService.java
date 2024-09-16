package bantads.airline.service;

import java.util.List;

import bantads.airline.dto.response.R11ResDTO;

public interface FlightService {
    // R11
    List<R11ResDTO> getflights();
}
