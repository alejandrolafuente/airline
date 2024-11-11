package bantads.airline.service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bantads.airline.dto.query.R05QueDTO;
import bantads.airline.dto.response.R03ResDTO;
import bantads.airline.dto.response.R05ResDTO;
import bantads.airline.exceptions.ClientNotFoundException;
import bantads.airline.model.Client;
import bantads.airline.model.MilesTransaction;
import bantads.airline.repository.ClientRepository;
import bantads.airline.repository.MilesTransactionRepository;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MilesTransactionRepository milesTransactionRepository;

    // R03 - 1
    @Override
    public R03ResDTO getMilesBalance(String userId) {

        Integer balance = clientRepository.getMilesBalanceByClientUserId(userId).orElseThrow(

                () -> new ClientNotFoundException("Client not found for User ID: " + userId));

        return new R03ResDTO(balance);
    }

    // R05
    @Override
    public R05ResDTO completeMilesPurchasing(String userId, R05QueDTO r05QueDTO) {

        Client client = clientRepository.getClientByUserId(userId);

        MilesTransaction transaction = MilesTransaction.builder()
                .client(client)
                .transactionDate(ZonedDateTime.now(ZoneId.of("UTC")))
                .moneyValue(r05QueDTO.getMoneyValue())
                .milesQuantity(r05QueDTO.getMoneyValue().divide(BigDecimal.valueOf(5)).intValue())
                .transactionType("INPUT")
                .description("MILES PURCHASE")
                .build();

        transaction = milesTransactionRepository.save(transaction);

        client.setMiles(client.getMiles() + transaction.getMilesQuantity()); // Adiciona as milhas ao cliente

        // Salva as alterações no cliente
        client = clientRepository.save(client);

        R05ResDTO dto = new R05ResDTO(client, transaction);

        return dto;
    }

}
