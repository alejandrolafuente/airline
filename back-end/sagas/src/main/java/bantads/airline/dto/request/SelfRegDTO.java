package bantads.airline.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelfRegDTO {
    private String cpf;
    private String name;
    private String email;
    private String addressType;
    private String number;
    private String complement;
    private String cep;
    private String city;
    private String state;
}
