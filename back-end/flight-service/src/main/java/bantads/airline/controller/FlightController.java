package bantads.airline.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bantads.airline.dto.request.R07QueDTO1;
import bantads.airline.dto.request.R15QueDTO;
import bantads.airline.dto.response.R03ResDTO;
import bantads.airline.dto.response.R04ResDTO;
import bantads.airline.dto.response.R07ResDTO1;
import bantads.airline.dto.response.R07ResDTO2;
import bantads.airline.dto.response.R15ResDTO;
import bantads.airline.service.FlightService;

@RestController
@CrossOrigin
@RequestMapping("flight")
public class FlightController {

    @Autowired
    private FlightService flightService;

    // R03 - 3
    @GetMapping("/client-flights")
    public ResponseEntity<List<R03ResDTO>> getClientflights(@RequestParam List<String> flightCodes) {
        List<R03ResDTO> dto = flightService.getClientFlights(flightCodes);
        return ResponseEntity.ok().body(dto);
    }

    // R04
    @GetMapping("/booking-flight/{code}")
    public ResponseEntity<R04ResDTO> getBookingFlight(@PathVariable(value = "code") String flightCode) {

        R04ResDTO dto = flightService.getBookingFlight(flightCode);
        return ResponseEntity.ok().body(dto);
    }

    // R07-1
    @GetMapping("/searchflights")
    public ResponseEntity<List<R07ResDTO1>> getClientRequestflights(@RequestBody R07QueDTO1 r07QueDTO1) {
        List<R07ResDTO1> dto = flightService.getClientRequestflights(r07QueDTO1);
        return ResponseEntity.ok().body(dto);
    }

    // R07 passo 2
    @GetMapping("/{id}")
    public ResponseEntity<R07ResDTO2> getFlight(@PathVariable(value = "id") String flightId) {
        R07ResDTO2 dto = flightService.getFlight(flightId);
        return ResponseEntity.ok().body(dto);

    }

    // R11
    @GetMapping("/flights")
    public ResponseEntity<?> getflights() {
        return new ResponseEntity<>(flightService.getflights(), HttpStatus.OK);
    }

    // R15
    @PostMapping("/insertflight")
    public ResponseEntity<R15ResDTO> insertflight(@RequestBody R15QueDTO r15QueDTO) {
        R15ResDTO dto = flightService.insertFlight(r15QueDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
