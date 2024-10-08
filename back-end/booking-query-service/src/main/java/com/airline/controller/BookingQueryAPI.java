package com.airline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airline.service.BookingQueryService;

@RestController
@CrossOrigin
@RequestMapping("bookingquery")
public class BookingQueryAPI {

    @Autowired
    private BookingQueryService bookingQueryService;

    // R03
    @GetMapping("/bookedflights/{id}")
    public ResponseEntity<?> getBookedFlights(@PathVariable(value = "id") String id) {
        return new ResponseEntity<>(bookingQueryService.findBookedFlights(id), HttpStatus.OK);
    }

}
