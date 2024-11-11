package bantads.airline.dto.response;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import bantads.airline.model.Client;
import bantads.airline.model.MilesTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R05ResDTO {
    private UUID transactionId;
    private String clientName;
    private String description;
    private Integer milesQuantity;
    private BigDecimal moneyValue;
    private String transactionDate;
    private String transactionTime;
    private Integer newMilesBalance;

    public R05ResDTO(Client client, MilesTransaction transaction) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        ZonedDateTime localTime = transaction.getTransactionDate().withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

        transactionId = transaction.getTransactionId();
        clientName = client.getName();
        description = transaction.getDescription();
        milesQuantity = transaction.getMilesQuantity();
        moneyValue = transaction.getMoneyValue();
        transactionDate = localTime.format(dateFormatter);
        transactionTime = localTime.format(timeFormatter);
        newMilesBalance = client.getMiles();
    }
}
// retornar: id da transacao, *nome do cliente, descricao da transacao,
// quantidade de milhas compradas, dinheiro, data da transacao, hora da
// transacao, *novo saldo de milhas,