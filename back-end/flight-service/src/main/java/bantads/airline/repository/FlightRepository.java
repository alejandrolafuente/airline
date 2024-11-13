package bantads.airline.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bantads.airline.model.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, UUID> {

        // R03
        // JPQL - Java Persistence Query Language
        @Query("select f from Flight f where f.code = ?1")
        Optional<Flight> getFlightByCode(String flightCode);

        // R11
        @Query(value = "SELECT * FROM flight_table f WHERE f.flight_date BETWEEN :currentDate AND :futureDate ORDER BY f.flight_date", nativeQuery = true)
        List<Flight> getNext48HoursFlights(@Param("currentDate") ZonedDateTime currentDate,
                        @Param("futureDate") ZonedDateTime futureDate);

        // R07 - 1
        @Query(value = "SELECT * FROM flight_table f WHERE f.flight_date  >= :currentDate AND f.departure_airport_id = :departure AND f.arrival_airport_id = :arrival ORDER BY f.flight_date", nativeQuery = true)
        List<Flight> getClientRequestflights(@Param("departure") UUID depAirport,
                        @Param("arrival") UUID arrAirportCode, @Param("currentDate") ZonedDateTime currentDate);

        // R07 - 1: Retorna todos os voos a partir de uma data
        @Query(value = "SELECT * FROM flight_table f WHERE f.flight_date >= :currentDate ORDER BY f.flight_date", nativeQuery = true)
        List<Flight> getAllFlightsAfterDate(@Param("currentDate") ZonedDateTime currentDate);

}
