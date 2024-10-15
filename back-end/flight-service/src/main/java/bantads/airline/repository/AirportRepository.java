package bantads.airline.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import bantads.airline.model.Airport;

@Repository
public interface AirportRepository extends JpaRepository<Airport, UUID> {

    @Query("select a from Airport a where a.code = ?1")
    Airport getAirportByCode(String code);
}
