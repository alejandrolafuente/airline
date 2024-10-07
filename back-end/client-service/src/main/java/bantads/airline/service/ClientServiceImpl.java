package bantads.airline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bantads.airline.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Integer getMilesBalance(String userId) {
        return clientRepository.getMilesBalanceByClientUserId(userId);
    }

}
