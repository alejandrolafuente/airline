package bantads.airline.service;

import java.util.List;

import bantads.airline.dto.request.R07QueDTO1;
import bantads.airline.dto.request.R15QueDTO;
import bantads.airline.dto.response.R03ResDTO;
import bantads.airline.dto.response.R04ResDTO;
import bantads.airline.dto.response.R07ResDTO1;
import bantads.airline.dto.response.R07ResDTO2;
import bantads.airline.dto.response.R11ResDTO;
import bantads.airline.dto.response.R15ResDTO;

public interface FlightService {

    // R03
    List<R03ResDTO> getClientFlights(List<String> flightCodes);

    // R04
    R04ResDTO getBookingFlight(String flightCode);

    // R07 - 1
    List<R07ResDTO1> getClientRequestflights(R07QueDTO1 dto);

    // R07 - 2
    R07ResDTO2 getFlight(String flightID);

    // R11
    List<R11ResDTO> getflights();

    // R15
    R15ResDTO insertFlight(R15QueDTO r15QueDTO);

}
