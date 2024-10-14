package bantads.airline.service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bantads.airline.dto.query.R05DTO;
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

    // R03
    @Override
    public Integer getMilesBalance(String userId) {
        return clientRepository.getMilesBalanceByClientUserId(userId);
    }

    // R05
    @Override
    public void completeMilesPurchasing(R05DTO r05dto) {

        System.out.println("CHEGA DO FRONT => " + r05dto);

        Client client = clientRepository.getClientByUserId(r05dto.getUserId());

        System.out.println("CLIENTE => " + client);

        // Obtém a data e hora local do servidor
        ZonedDateTime localDateTime = ZonedDateTime.now(ZoneId.systemDefault());

        // Converte para UTC antes de armazenar no banco de dados
        ZonedDateTime utcDateTime = localDateTime.withZoneSameInstant(ZoneOffset.UTC);

        MilesTransaction transaction = MilesTransaction.builder()
                .client(client)
                .transactionDate(utcDateTime)
                .moneyValue(r05dto.getMoneyValue())
                .milesQuantity(r05dto.getMoneyValue().divide(BigDecimal.valueOf(5)).intValue())
                .transactionType("INPUT")
                .description("MILES PURCHASE")
                .build();

        milesTransactionRepository.save(transaction);

        ZonedDateTime utc1DateTime = transaction.getTransactionDate(); // Data em UTC
        ZonedDateTime local1DateTime = utc1DateTime.withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));
        System.out.println("Hora local: " + local1DateTime);

        client.setMiles(client.getMiles() + transaction.getMilesQuantity()); // Adiciona as milhas ao cliente

        // Salva as alterações no cliente
        clientRepository.save(client);
    }

}
