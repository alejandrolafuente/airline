package com.airline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airline.dto.response.R04ResDTO;
import com.airline.service.BookingQueryService;

@RestController
@CrossOrigin
@RequestMapping("bookingquery")
public class BookingQueryAPI {

    @Autowired
    private BookingQueryService bookingQueryService;

    // R03 - 2
    @GetMapping("/bookedflights/{id}")
    public ResponseEntity<?> getClientBookings(@PathVariable(value = "id") String id) {
        return new ResponseEntity<>(bookingQueryService.findClientBookings(id), HttpStatus.OK);
    }

    // R04
    @GetMapping("/booking/{id}")
    public ResponseEntity<R04ResDTO> getBooking(@PathVariable(value = "id") String bookingId) {
        R04ResDTO dto = bookingQueryService.getBooking(bookingId);
        return ResponseEntity.ok().body(dto);
    }
}
