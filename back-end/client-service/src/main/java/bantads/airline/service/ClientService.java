package bantads.airline.service;

import bantads.airline.dto.query.R05DTO;

public interface ClientService {

    // R03
    Integer getMilesBalance(String userId);

    // R05
    void completeMilesPurchasing(R05DTO r05dto);
}
