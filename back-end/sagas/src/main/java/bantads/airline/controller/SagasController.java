package bantads.airline.controller;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import bantads.airline.dto.request.BookingQueryDTO;
import bantads.airline.dto.request.NewEmployeeDTO;
import bantads.airline.dto.request.PutEmpDTO;
import bantads.airline.dto.request.SelfRegDTO;
import bantads.airline.dto.response.GenResDTO;
import bantads.airline.sagas.bookingsaga.BookingSAGA;
import bantads.airline.sagas.cancelbookinsaga.CancelBookingSaga;
import bantads.airline.sagas.cancelflightsaga.CancelFlightSaga;
import bantads.airline.sagas.completeflightsaga.CompleteFlightSaga;
import bantads.airline.sagas.deletemployeesaga.DelEmpSaga;
import bantads.airline.sagas.registeremployeesaga.RegisterEmployeeSaga;
import bantads.airline.sagas.selfregistersaga.ManageRegisterQuery;
import bantads.airline.sagas.selfregistersaga.SelfRegisterSAGA;
import bantads.airline.sagas.selfregistersaga.dto.ManageRegisterRes;
import bantads.airline.sagas.updateemployeesaga.UpdateEmpSaga;
import bantads.airline.utils.ValidateCPF;

@RestController
@CrossOrigin
@RequestMapping("saga")
public class SagasController {

    @Autowired
    private ValidateCPF validateCPF;

    @Autowired
    private SelfRegisterSAGA selfRegisterSAGA;

    @Autowired
    private BookingSAGA bookingSAGA;

    @Autowired
    private CompleteFlightSaga completeFlightSaga;

    @Autowired
    private CancelBookingSaga cancelBookingSaga;

    @Autowired
    private CancelFlightSaga cancelFlightSaga;

    @Autowired
    private RegisterEmployeeSaga registerEmployeeSaga;

    @Autowired
    private UpdateEmpSaga updateEmpSaga;

    @Autowired
    private DelEmpSaga delEmpSaga;

    @Autowired
    private ManageRegisterQuery manageRegisterQuery;

    // R01
    @PostMapping("/register")
    public ResponseEntity<?> registerRequest(@RequestBody SelfRegDTO selfRegDTO)
            throws JsonProcessingException, InterruptedException, ExecutionException {

        System.out.println("REQUEST: " + selfRegDTO);

        if (!validateCPF.validateCPF(selfRegDTO.getCpf())) {// if cpf not valid...

            return new ResponseEntity<>("Invalid CPF!", HttpStatus.BAD_REQUEST);
        }

        CompletableFuture<ManageRegisterRes> future = manageRegisterQuery.manageQuery(selfRegDTO.getCpf(),
                selfRegDTO.getEmail());

        // awaits for response in blocking mode
        ManageRegisterRes res = future.get();

        if (res.getStartSaga()) {

            this.selfRegisterSAGA.handleRequest(selfRegDTO);

            return new ResponseEntity<>("Your request has been sent, check your email", HttpStatus.OK);
        } else {

            return new ResponseEntity<>(res.getResponse(), HttpStatus.BAD_REQUEST);
        }

    }

    // R07 - Booking
    @PostMapping("/booking")
    public ResponseEntity<GenResDTO> flightBooking(@RequestBody BookingQueryDTO bookingQueryDTO)
            throws JsonProcessingException, InterruptedException, ExecutionException {

        this.bookingSAGA.handleRequest(bookingQueryDTO);

        GenResDTO dto = new GenResDTO("Flight reservation made");

        return ResponseEntity.ok().body(dto);

    }

    // R08 - Cancel Booking
    @PutMapping("/cancel-booking/{id}")
    public ResponseEntity<GenResDTO> cancelBooking(@PathVariable("id") UUID bookingId) throws JsonProcessingException {

        cancelBookingSaga.handleRequest(bookingId);

        GenResDTO dto = new GenResDTO("Booking Cancelled");

        return ResponseEntity.ok().body(dto);

    }

    // R13 - Cancelamento do Voo
    @PutMapping("/cancel-flight/{id}")
    public ResponseEntity<GenResDTO> cancelFlight(@PathVariable("id") UUID flightId) throws JsonProcessingException {

        cancelFlightSaga.handleRequest(flightId);

        GenResDTO dto = new GenResDTO("Flight Cancelled");

        return ResponseEntity.ok().body(dto);
    }

    // R14 - Realização do Voo
    @PutMapping("/complete-flight/{id}")
    public ResponseEntity<GenResDTO> completeFlight(@PathVariable("id") UUID flightId) throws JsonProcessingException {

        this.completeFlightSaga.handleRequest(flightId);

        GenResDTO dto = new GenResDTO("Flight Completed");

        return ResponseEntity.ok().body(dto);
    }

    // R17
    @PostMapping("/add-employee")
    public ResponseEntity<GenResDTO> registerEmployee(@RequestBody NewEmployeeDTO newEmployeeDTO)
            throws JsonProcessingException, InterruptedException, ExecutionException {

        if (!validateCPF.validateCPF(newEmployeeDTO.getCpf())) {// if cpf not valid...
            GenResDTO dto = new GenResDTO("Invalid CPF!");
            return ResponseEntity.badRequest().body(dto);
        }

        CompletableFuture<ManageRegisterRes> future = manageRegisterQuery.newEmployeeQuery(newEmployeeDTO.getCpf(),
                newEmployeeDTO.getEmail());

        // awaits for response in blocking mode
        ManageRegisterRes res = future.get();

        if (res.getStartSaga()) {
            System.out.println(res.getResponse());
            this.registerEmployeeSaga.handleRequest(newEmployeeDTO);
            GenResDTO dto = new GenResDTO("Your request has been sent, check your email");
            return ResponseEntity.ok().body(dto);
        } else {
            GenResDTO dto = new GenResDTO(res.getResponse());
            return ResponseEntity.badRequest().body(dto);
        }
    }

    // R18
    @PutMapping("/update/{id}")
    public ResponseEntity<GenResDTO> updateEmployee(@PathVariable("id") String userId, @RequestBody PutEmpDTO putEmpDTO)
            throws JsonProcessingException {

        this.updateEmpSaga.handleRequest(putEmpDTO);

        GenResDTO dto = new GenResDTO("Employee Data Updated");

        return ResponseEntity.ok().body(dto);
    }

    // R19
    @DeleteMapping("delete/{id}")
    public ResponseEntity<GenResDTO> removeEmployee(@PathVariable("id") String userId) throws JsonProcessingException {

        this.delEmpSaga.handleRequest(userId);

        GenResDTO dto = new GenResDTO("Employee with user id " + userId + " was deleted");

        return ResponseEntity.ok().body(dto);
    }

}
