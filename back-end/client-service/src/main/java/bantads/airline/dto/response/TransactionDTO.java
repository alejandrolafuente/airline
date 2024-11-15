package bantads.airline.dto.response;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import bantads.airline.model.MilesTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private UUID transactionId;
    private String transactionDate;
    private String transactionTime;
    private BigDecimal moneyValue;
    private Integer milesQuantity;
    private String description;

    public TransactionDTO(MilesTransaction transaction) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        ZonedDateTime localTime = transaction.getTransactionDate().withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

        transactionId = transaction.getTransactionId();
        transactionDate = localTime.format(dateFormatter);
        transactionTime = localTime.format(timeFormatter);
        moneyValue = transaction.getMoneyValue();
        milesQuantity = transaction.getMilesQuantity();
        description = transaction.getDescription();
    }
}
