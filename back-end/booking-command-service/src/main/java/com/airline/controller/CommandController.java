package com.airline.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airline.service.CommandService;

@RestController
@CrossOrigin
@RequestMapping("booking-command")
public class CommandController {

    // R10: Fazer Check-In
    // apenas o id da reserva basta

    @Autowired
    private CommandService commandService;

    @PostMapping("/purchase/{id}")
    public ResponseEntity<?> milesPurhase(@PathVariable(value = "id") String id) {

        UUID bookingId = UUID.fromString(id);

        commandService.doCheckIn(bookingId);

        return null;
    }

}
