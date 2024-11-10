package bantads.airline.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class R03ResDTO {
    private Integer milesBalance;

    public R03ResDTO(Integer balance) {
        milesBalance = balance;
    }
}
