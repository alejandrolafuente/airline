package bantads.airline.sagas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bantads.airline.model.Client;
import bantads.airline.repository.ClientRepository;
import bantads.airline.sagas.commands.CreateClientCommand;
import bantads.airline.sagas.commands.UpdateMilesCommand;
import bantads.airline.sagas.events.ClientCreatedEvent;
import bantads.airline.sagas.events.MilesUpdatedEvent;
import bantads.airline.sagas.queries.ManageRegisterRes;
import bantads.airline.sagas.queries.VerifyClientQuery;

@Service
public class SagaService {

    @Autowired
    private ClientRepository clientRepository;

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
    public MilesUpdatedEvent updateMiles(UpdateMilesCommand updateMilesCommand) {

        Client client = clientRepository.getClientByUserId(updateMilesCommand.getUserId());

        client.setMiles(client.getMiles() - updateMilesCommand.getUsedMiles());

        client = clientRepository.save(client);

        MilesUpdatedEvent event = MilesUpdatedEvent.builder()
                .milesBalance(client.getMiles())
                .messageType("MilesUpdatedEvent")
                .build();

        return event;
    }
}
