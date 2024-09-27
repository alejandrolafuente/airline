package bantads.airline.sagas.selfregistersaga;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bantads.airline.sagas.selfregistersaga.dto.ManageRegisterRes;
import bantads.airline.sagas.selfregistersaga.queries.VerifyClientQuery;

@Component
public class ManageRegisterQuery {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final Map<String, CompletableFuture<ManageRegisterRes>> futureMap = new ConcurrentHashMap<>();

    public CompletableFuture<ManageRegisterRes> manageQuery(String cpf, String email) throws JsonProcessingException {

        VerifyClientQuery verifyClientQuery = VerifyClientQuery.builder()
                .clientCpf(cpf)
                .clientEmail(email)
                .messageType("VerifyClientQuery")
                .build();

        var sendingMessage = objectMapper.writeValueAsString(verifyClientQuery);

        CompletableFuture<ManageRegisterRes> future = new CompletableFuture<>();

        futureMap.put(cpf, future);

        rabbitTemplate.convertAndSend("VerifyClientRequestChannel", sendingMessage);

        return future;
    }

    @RabbitListener(queues = "VerifyClientReturnChannel")
    public void handleMessage(String msg) throws JsonProcessingException, JsonMappingException {

        ManageRegisterRes response = objectMapper.readValue(msg, ManageRegisterRes.class);

        CompletableFuture<ManageRegisterRes> future = futureMap.remove(response.getCpf());

        if (future != null) {
            future.complete(response);
        } else {
            System.err.println("No CompletableFuture found for CPF: " + response.getCpf());
        }

    }
}
