package bantads.airline.dto.query;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R05DTO {
    private BigDecimal moneyValue;
    private String userId;
}
