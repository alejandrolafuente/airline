package com.airline.service;


import com.airline.dto.R10R12ResDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CommandService {

    // R10, R12, ...
    R10R12ResDTO updateBookingStatus(String identifier, int statusCode) throws JsonProcessingException;

}
