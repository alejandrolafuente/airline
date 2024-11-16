package bantads.airline.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class GenResDTO {
    private String message;

    public GenResDTO(String msg) {
        message = msg;
    }
}
