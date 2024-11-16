package bantads.airline.service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bantads.airline.dto.query.R05QueDTO;
import bantads.airline.dto.response.R03ResDTO;
import bantads.airline.dto.response.R05ResDTO;
import bantads.airline.dto.response.R06ResDTO;
import bantads.airline.dto.response.TransactionDTO;
import bantads.airline.exceptions.ClientNotFoundException;
import bantads.airline.exceptions.ClientUpdateException;
import bantads.airline.exceptions.MilesTransactionException;
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

        Client client = clientRepository.getClientByUserId(userId).orElseThrow(

                () -> new ClientNotFoundException("Client not found for User ID: " + userId));

        MilesTransaction transaction = MilesTransaction.builder()
                .client(client)
                .transactionDate(ZonedDateTime.now(ZoneId.of("UTC")))
                .moneyValue(r05QueDTO.getMoneyValue())
                .milesQuantity(r05QueDTO.getMoneyValue().divide(BigDecimal.valueOf(5)).intValue())
                .transactionType("INPUT")
                .description("MILES PURCHASE")
                .build();

        // Faz a transacao
        try {
            transaction = milesTransactionRepository.save(transaction);
        } catch (Exception e) {
            throw new MilesTransactionException("Cannot complete miles purchasing");
        }

        client.setMiles(client.getMiles() + transaction.getMilesQuantity()); // Adiciona as milhas ao cliente

        // Salva as alterações no cliente
        try {

            client = clientRepository.save(client);
        } catch (Exception e) {

            throw new ClientUpdateException("Cannot update miles balance for ID: " + client.getUserId());
        }

        R05ResDTO dto = new R05ResDTO(client, transaction);

        return dto;
    }

    // R06 - 1
    @Override
    public R06ResDTO getMilesStatement(String userId) {

        Integer milesBalance = this.getMilesBalance(userId).getMilesBalance();

        Client client = clientRepository.getClientByUserId(userId).orElseThrow(

                () -> new ClientNotFoundException("Client not found for User ID: " + userId));

        List<MilesTransaction> milesTransactions = milesTransactionRepository.getClientTransactions(client.getClientId());

        List<TransactionDTO> transactionDtos = milesTransactions.stream().map(TransactionDTO::new).toList();

        R06ResDTO dto = R06ResDTO.builder()
                .milesBalance(milesBalance)
                .clientTransactions(transactionDtos)
                .build();

        return dto;
    }

}
