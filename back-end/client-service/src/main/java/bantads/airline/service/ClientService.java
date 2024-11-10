package bantads.airline.service;

import bantads.airline.dto.query.R05DTO;
import bantads.airline.dto.response.R03ResDTO;

public interface ClientService {

    // R03 - 1
    R03ResDTO getMilesBalance(String userId);

    // R05
    void completeMilesPurchasing(R05DTO r05dto);
}
