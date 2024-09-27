package bantads.airline.sagas.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateClientCommand {
    private String userId; // reference from authentication service
    private String cpf;
    private String name;
    private String email;
    private String addressType;
    private String number;
    private String complement;
    private String cep;
    private String city;
    private String state;
    private String messageType;
}
