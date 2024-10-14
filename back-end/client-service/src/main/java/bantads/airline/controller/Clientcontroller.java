package bantads.airline.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bantads.airline.dto.query.R05DTO;
import bantads.airline.dto.query.R05QueDTO;
import bantads.airline.service.ClientService;

@RestController
@CrossOrigin
@RequestMapping("client")
public class Clientcontroller {

    @Autowired
    private ClientService clientService;

    // R03
    @GetMapping("/balance/{id}")
    public ResponseEntity<?> getBalance(@PathVariable(value = "id") String userId) {
        return new ResponseEntity<>(clientService.getMilesBalance(userId), HttpStatus.OK);
    }

    // R05
    @PostMapping("/purchase/{id}")
    public ResponseEntity<?> milesPurhase(@PathVariable(value = "id") String userId, @RequestBody R05QueDTO r05QueDTO) {

        R05DTO dto = R05DTO.builder()
                .moneyValue(new BigDecimal(r05QueDTO.getMoneyValue()))
                .userId(userId)
                .build();

        clientService.completeMilesPurchasing(dto);

        return new ResponseEntity<>("Mile purchase completed successfully.", HttpStatus.OK);
        // return new ResponseEntity<>("Miles purchase completed successfully.",
        // HttpStatus.OK);
    }

}
