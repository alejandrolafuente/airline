package bantads.airline.service;

import bantads.airline.dto.query.R05QueDTO;
import bantads.airline.dto.response.R03ResDTO;
import bantads.airline.dto.response.R05ResDTO;
import bantads.airline.dto.response.R06ResDTO;

public interface ClientService {

    // R03 - 1
    R03ResDTO getMilesBalance(String userId);

    // R05
    R05ResDTO completeMilesPurchasing(String userId, R05QueDTO r05QueDTO);

    // R06
    R06ResDTO getMilesStatement(String userId);

}
