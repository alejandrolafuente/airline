package com.airline.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airline.dto.R10R12ResDTO;
import com.airline.service.CommandService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@CrossOrigin
@RequestMapping("booking-command")
public class CommandController {

    @Autowired
    private CommandService commandService;

    // apenas o id da reserva basta
    // provavelmente api composition antes

    // R10: Fazer Check-In
    @PutMapping("/check-in/{id}")
    public ResponseEntity<R10R12ResDTO> doCheckIn(@PathVariable(value = "id") UUID bookingId) throws JsonProcessingException {
        R10R12ResDTO dto = commandService.updateBookingStatus(bookingId.toString(), 2); // 2 é o código para check-in
        return ResponseEntity.ok().body(dto);
    }

    // R12: Confirmação de Embarque
    @PutMapping("/board-passenger/{code}")
    public ResponseEntity<R10R12ResDTO> boardPassenger(@PathVariable(value = "code") String bookingCode)
            throws JsonProcessingException {
        R10R12ResDTO dto =  commandService.updateBookingStatus(bookingCode, 4); // 4 é o código para embarque
        return ResponseEntity.ok().body(dto);
    }
}
