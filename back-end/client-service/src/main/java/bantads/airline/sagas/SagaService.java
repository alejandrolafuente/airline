package bantads.airline.sagas;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bantads.airline.model.Client;
import bantads.airline.model.MilesTransaction;
import bantads.airline.repository.ClientRepository;
import bantads.airline.repository.MilesTransactionRepository;
import bantads.airline.sagas.commands.CreateClientCommand;
import bantads.airline.sagas.commands.RefundClientCommand;
import bantads.airline.sagas.commands.UpdateMilesCommand;
import bantads.airline.sagas.events.ClientCreatedEvent;
import bantads.airline.sagas.events.ClientRefundedEvent;
import bantads.airline.sagas.events.MilesUpdatedEvent;
import bantads.airline.sagas.queries.ManageRegisterRes;
import bantads.airline.sagas.queries.VerifyClientQuery;
import jakarta.transaction.Transactional;

@Service
public class SagaService {

        @Autowired
        private ClientRepository clientRepository;

        @Autowired
        private MilesTransactionRepository milesTransactionRepository;

        // R01 - saga
        public ManageRegisterRes verifyClient(VerifyClientQuery query) {

                Client client = clientRepository.findByCpfAndEmail(query.getClientCpf(), query.getClientEmail());

                if (client != null) {

                        return ManageRegisterRes.builder()
                                        .cpf(client.getCpf())
                                        .response("You are a client already")
                                        .startSaga(false)
                                        .build();

                }

                client = clientRepository.getClientByCpf(query.getClientCpf());

                if (client != null) {

                        return ManageRegisterRes.builder()
                                        .cpf(query.getClientCpf())
                                        .response("This CPF is registered in the system")
                                        .startSaga(false)
                                        .build();
                }

                client = clientRepository.getClientByEmail(query.getClientEmail());

                if (client != null) {

                        return ManageRegisterRes.builder()
                                        .cpf(query.getClientCpf())
                                        .response("This email is registered in the system")
                                        .startSaga(false)
                                        .build();
                }

                return ManageRegisterRes.builder()
                                .cpf(query.getClientCpf())
                                .response("Must start self-register SAGA")
                                .startSaga(true)
                                .build();

        }

        public ClientCreatedEvent saveNewClient(CreateClientCommand command) {

                System.out.println("COMANDO CHEGOU => " + command);

                Client newClient = Client.builder()
                                .userId(command.getUserId())
                                .cpf(command.getCpf())
                                .name(command.getName())
                                .email(command.getEmail())
                                .addressType(command.getAddressType())
                                .number(command.getNumber())
                                .complement(command.getComplement())
                                .cep(command.getCep())
                                .city(command.getCity())
                                .state(command.getState())
                                .miles(0)
                                .build();

                this.clientRepository.save(newClient);

                ClientCreatedEvent clientCreatedEvent = ClientCreatedEvent.builder()
                                .messageType("ClientCreatedEvent")
                                .build();

                return clientCreatedEvent;
        }

        // R07
        @Transactional
        public MilesUpdatedEvent updateMiles(UpdateMilesCommand updateMilesCommand) {

                if (updateMilesCommand.getUsedMiles() == null) {
                        updateMilesCommand.setUsedMiles(0);
                }

                if (updateMilesCommand.getMoneyValue() == null) {
                        updateMilesCommand.setMoneyValue(BigDecimal.ZERO);
                }

                Client client = clientRepository.getClientByUserId(updateMilesCommand.getUserId()).orElseThrow(null);

                // primeiro vamos fazer a transação e depois atualizamos o saldo do cliente

                MilesTransaction transaction = MilesTransaction.builder()
                                .client(client)
                                .transactionDate(ZonedDateTime.now(ZoneId.of("UTC")))
                                .moneyValue(updateMilesCommand.getMoneyValue())
                                .milesQuantity(updateMilesCommand.getUsedMiles())
                                .transactionType("OUTPUT")
                                .description("FLIGHT BOOKING")
                                .build();

                transaction = milesTransactionRepository.save(transaction);

                // atualiza saldo do cliente
                client.setMiles(client.getMiles() - transaction.getMilesQuantity());

                client = clientRepository.save(client);

                // prepara mensagem de volta para o orquestrador
                MilesUpdatedEvent event = MilesUpdatedEvent.builder()
                                .transactionId(transaction.getTransactionId())
                                .milesBalance(client.getMiles())
                                .messageType("MilesUpdatedEvent")
                                .build();

                return event;
        }

        // R08, R13
        @Transactional
        public ClientRefundedEvent refundClient(RefundClientCommand refundClientCommand) {

                Client client = clientRepository.getClientByUserId(refundClientCommand.getUserId()).orElseThrow(null);

                MilesTransaction transaction = MilesTransaction.builder()
                                .client(client)
                                .transactionDate(ZonedDateTime.now(ZoneId.of("UTC")))
                                .moneyValue(refundClientCommand.getRefundMoney())
                                .milesQuantity(refundClientCommand.getRefundMiles())
                                .transactionType("INPUT")
                                .description("MILES REFUND")
                                .build();

                transaction = milesTransactionRepository.save(transaction);

                // updates balance
                client.setMiles(client.getMiles() + transaction.getMilesQuantity());

                client = clientRepository.save(client);

                ClientRefundedEvent clientRefundedEvent = ClientRefundedEvent.builder()
                                .saga(refundClientCommand.getSaga())
                                .name(client.getName())
                                .refundMoney(transaction.getMoneyValue())
                                .refundMiles(transaction.getMilesQuantity())
                                .newBalance(client.getMiles())
                                .messageType("ClientRefundedEvent")
                                .build();

                return clientRefundedEvent;

        }
}
