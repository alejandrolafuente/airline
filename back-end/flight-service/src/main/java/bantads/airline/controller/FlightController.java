package bantads.airline.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bantads.airline.dto.request.R07QueDTO1;
import bantads.airline.dto.request.R15QueDTO;
import bantads.airline.service.FlightService;

@RestController
@CrossOrigin
@RequestMapping("flight")
public class FlightController {

    @Autowired
    private FlightService flightService;

    // R03
    @GetMapping("/bookedflights")
    public ResponseEntity<?> getBookedflights(@RequestParam List<String> flightCodes) {
        return new ResponseEntity<>(flightService.getBookedFlights(flightCodes), HttpStatus.OK);
    }

    // R07 - 1
    @GetMapping("/searchflights")
    public ResponseEntity<?> getClientRequestflights(@RequestBody R07QueDTO1 dto) {

        return new ResponseEntity<>(flightService.getClientRequestflights(dto), HttpStatus.OK);
    }

    // R11
    @GetMapping("/flights")
    public ResponseEntity<?> getflights() {
        return new ResponseEntity<>(flightService.getflights(), HttpStatus.OK);
    }

    // R15
    @PostMapping("/insertflight")
    public ResponseEntity<?> insertflight(@RequestBody R15QueDTO r15QueDTO) {

        return new ResponseEntity<>(flightService.insertFlight(r15QueDTO), HttpStatus.OK);
    }
}
