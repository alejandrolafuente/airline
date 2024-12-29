package bantads.airline.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEmployeeDTO {
    private String name;
    private String cpf;
    private String email;
    private String phoneNumber;
}
