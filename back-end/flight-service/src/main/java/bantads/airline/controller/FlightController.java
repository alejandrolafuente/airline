package bantads.airline.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bantads.airline.model.Airport;
import bantads.airline.service.FlightService;

@RestController
@CrossOrigin
@RequestMapping("flight")
public class FlightController {

    @Autowired
    private FlightService flightService;

    // R11
    @GetMapping("/flights")
    public ResponseEntity<?> getflights() {
        return new ResponseEntity<>(flightService.getflights(), HttpStatus.OK);
    }

    // api composition teste - deletar
    @GetMapping("/airports/{id}")
    public List<Airport> getAllAirports(@PathVariable(value = "id") String id) {
        return flightService.getAllAirports();
    }

}
