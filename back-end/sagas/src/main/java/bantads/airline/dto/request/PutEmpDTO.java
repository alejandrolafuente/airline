package bantads.airline.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PutEmpDTO {
    private String userID;
    private String name;
    private String email;
    private String phoneNumber;
}
