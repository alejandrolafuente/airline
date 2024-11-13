package bantads.airline.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class R07ResDTO {
    private String message;

    public R07ResDTO(String msg) {
        message = msg;
    }
}
