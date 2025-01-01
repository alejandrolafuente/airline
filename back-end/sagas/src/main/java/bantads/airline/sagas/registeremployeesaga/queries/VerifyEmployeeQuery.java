package bantads.airline.sagas.registeremployeesaga.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyEmployeeQuery {
    private String employeeCpf;
    private String employeeEmail;
    private String messageType;
}
