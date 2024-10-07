package bantads.airline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
