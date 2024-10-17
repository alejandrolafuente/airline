package bantads.airline.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R07ResDTO1 {
    private String flightId;// devolver o id para o front fazer a busca ao selecionar voo
    private String flightDate;
    private String flighTime;
    private String departure; // ex g. "AEROPORTO DE GUARULHOS"
    private String arrival; // ex g. "AEROPORTO GALEÃO"
}
