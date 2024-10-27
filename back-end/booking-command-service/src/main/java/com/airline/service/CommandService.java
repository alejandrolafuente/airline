package com.airline.service;

import java.util.UUID;

public interface CommandService {

    void doCheckIn(UUID bookingId);

}
