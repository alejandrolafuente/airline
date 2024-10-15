package bantads.airline.service;

import java.util.List;

import bantads.airline.dto.request.R15QueDTO;
import bantads.airline.dto.response.R03ResDTO;
import bantads.airline.dto.response.R11ResDTO;
import bantads.airline.dto.response.R15ResDTO;

public interface FlightService {

    // R03
    List<R03ResDTO> getBookedFlights(List<String> flightCodes);

    // R11
    List<R11ResDTO> getflights();

    // R15
    R15ResDTO insertFlight(R15QueDTO r15QueDTO);

}
