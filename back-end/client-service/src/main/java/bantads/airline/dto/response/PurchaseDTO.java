package bantads.airline.dto.response;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import bantads.airline.model.MilesTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDTO {
    private String purchaseDate;
    private String purchaseTime;
    private BigDecimal moneyValue;
    private Integer milesQuantity;
    private String description;

    public PurchaseDTO(MilesTransaction purchase) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        ZonedDateTime localTime = purchase.getTransactionDate().withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

        purchaseDate = localTime.format(dateFormatter);
        purchaseTime = localTime.format(timeFormatter);
        moneyValue = purchase.getMoneyValue();
        milesQuantity = purchase.getMilesQuantity();
        description = purchase.getDescription();
    }
}
