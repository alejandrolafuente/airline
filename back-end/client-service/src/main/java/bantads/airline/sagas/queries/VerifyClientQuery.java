package bantads.airline.sagas.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyClientQuery {
    private String clientCpf;
    private String clientEmail;
    private String messageType;
}
