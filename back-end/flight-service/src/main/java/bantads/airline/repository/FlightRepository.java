package bantads.airline.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bantads.airline.model.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, UUID> {

}
