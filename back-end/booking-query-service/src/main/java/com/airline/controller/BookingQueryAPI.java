package com.airline.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airline.dto.response.R03ResDTO;
import com.airline.dto.response.R04ResDTO;
import com.airline.service.BookingQueryService;

@RestController
@CrossOrigin
@RequestMapping("booking-query")
public class BookingQueryAPI {

    @Autowired
    private BookingQueryService bookingQueryService;

    // R03 - 2
    @GetMapping("/client-bookings/{id}")
    public ResponseEntity<List<R03ResDTO>> getClientBookings(@PathVariable(value = "id") String userId) {
        List<R03ResDTO> dto = bookingQueryService.findClientBookings(userId);
        return ResponseEntity.ok().body(dto);
    }

    // R04
    @GetMapping("/booking/{id}")
    public ResponseEntity<R04ResDTO> getBooking(@PathVariable(value = "id") String bookingId) {
        R04ResDTO dto = bookingQueryService.getBooking(bookingId);
        return ResponseEntity.ok().body(dto);
    }
}
