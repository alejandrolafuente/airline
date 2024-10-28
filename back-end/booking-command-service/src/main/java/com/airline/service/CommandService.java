package com.airline.service;


import com.fasterxml.jackson.core.JsonProcessingException;

public interface CommandService {

    // R10, R12, ...
    void updateBookingStatus(String identifier, int statusCode) throws JsonProcessingException;

}
