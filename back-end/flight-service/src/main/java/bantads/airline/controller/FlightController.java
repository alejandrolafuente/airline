package bantads.airline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bantads.airline.service.FlightService;

@RestController
@CrossOrigin
@RequestMapping("flight")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/flights")
    public ResponseEntity<?> getflights() {
        flightService.getflights();
        return null;
    }

}
