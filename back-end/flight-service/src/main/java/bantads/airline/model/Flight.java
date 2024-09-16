package bantads.airline.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "flight_table")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "flight_id")
    private UUID flightId;

    @Column(name = "code")
    private String code; // e.g. "TADS8971"

    @Column(name = "flight_date")
    private ZonedDateTime flightDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departure_airport_id", referencedColumnName = "airport_id", nullable = false)
    private Airport departureAirport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "arrival_airport_id", referencedColumnName = "airport_id", nullable = false)
    private Airport arrivalAirport;

    @Column(name = "flight_price")
    private BigDecimal flightPrice;

    @Column(name = "total_seats")
    private Integer totalSeats;

    @Column(name = "occupied_seats")
    private Integer occupiedSeats;

    @Column(name = "flight_status")
    private String flightStatus;

}