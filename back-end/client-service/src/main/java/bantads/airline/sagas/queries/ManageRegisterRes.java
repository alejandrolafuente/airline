package bantads.airline.sagas.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManageRegisterRes {
    private String cpf;
    private String response;
    private Boolean startSaga;
}
