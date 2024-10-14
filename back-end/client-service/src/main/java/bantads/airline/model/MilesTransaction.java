package bantads.airline.model;

import java.io.Serializable;
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
@Table(name = "transaction_table")
public class MilesTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id", nullable = false, updatable = false)
    private UUID transactionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", referencedColumnName = "client_id", nullable = false)
    private Client client;

    @Column(name = "transaction_date", nullable = false)
    private ZonedDateTime transactionDate;

    @Column(name = "money_value", nullable = true)
    private BigDecimal moneyValue;

    @Column(name = "miles_quantity", nullable = false)
    private Integer milesQuantity;

    @Column(name = "transaction_type", nullable = false)
    private String transactionType; // "INPUT"/"OUTPUT"

    @Column(name = "description", nullable = false)
    private String description; // "MILES PURCHASE"/"TICKET BOOKING"/"MILES REFUND"

}
