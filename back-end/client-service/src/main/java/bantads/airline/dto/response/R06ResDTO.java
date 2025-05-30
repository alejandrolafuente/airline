package bantads.airline.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R06ResDTO {
    private Integer milesBalance;
    private List<TransactionDTO> clientTransactions;
}
