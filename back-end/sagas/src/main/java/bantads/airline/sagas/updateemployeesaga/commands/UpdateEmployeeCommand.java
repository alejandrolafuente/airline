package bantads.airline.sagas.updateemployeesaga.commands;

import bantads.airline.dto.request.PutEmpDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEmployeeCommand {
    private String userID;
    private String name;
    private String email;
    private String phoneNumber;

    public UpdateEmployeeCommand(PutEmpDTO entity) {
        this.userID = entity.getUserID();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
    }
}
