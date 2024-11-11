package bantads.airline.controller;

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

import bantads.airline.dto.query.R05QueDTO;
import bantads.airline.dto.response.R03ResDTO;
import bantads.airline.dto.response.R05ResDTO;
import bantads.airline.service.ClientService;

@RestController
@CrossOrigin
@RequestMapping("client")
public class Clientcontroller {

    @Autowired
    private ClientService clientService;

    // R03 - 1
    @GetMapping("/balance/{id}")
    public ResponseEntity<R03ResDTO> getBalance(@PathVariable(value = "id") String userId) {

        R03ResDTO dto = clientService.getMilesBalance(userId);

        return ResponseEntity.ok().body(dto);
    }

    // R05
    @PostMapping("/purchase/{id}")
    public ResponseEntity<R05ResDTO> milesPurhase(@PathVariable(value = "id") String userId,
            @RequestBody R05QueDTO r05QueDTO) {

        R05ResDTO dto = clientService.completeMilesPurchasing(userId, r05QueDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);

    }

}
