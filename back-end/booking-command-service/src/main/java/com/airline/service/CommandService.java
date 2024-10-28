package com.airline.service;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface CommandService {

    // R10
    void doCheckIn(UUID bookingId) throws JsonProcessingException;

    // R12
    void boardPassenger(String bookingCode) throws JsonProcessingException;

}
