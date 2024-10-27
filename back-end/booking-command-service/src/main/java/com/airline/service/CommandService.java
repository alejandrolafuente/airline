package com.airline.service;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface CommandService {

    void doCheckIn(UUID bookingId) throws JsonProcessingException;

}
