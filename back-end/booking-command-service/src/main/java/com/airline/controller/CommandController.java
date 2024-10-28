package com.airline.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airline.service.CommandService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@CrossOrigin
@RequestMapping("booking-command")
public class CommandController {

    @Autowired
    private CommandService commandService;

    // R10: Fazer Check-In ; apenas o id da reserva basta
    // provavelmente api composition antes
    
    @PutMapping("/check-in/{id}")
    public ResponseEntity<?> doCheckIn(@PathVariable(value = "id") String id) throws JsonProcessingException {

        UUID bookingId = UUID.fromString(id);

        commandService.doCheckIn(bookingId);

        return null;
    }

}
