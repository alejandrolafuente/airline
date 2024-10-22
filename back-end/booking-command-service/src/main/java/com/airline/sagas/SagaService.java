package com.airline.sagas;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.model.Booking;
import com.airline.repository.BookingRepository;
import com.airline.sagas.commands.CreateBookingCommand;

@Service
public class SagaService {

    // class CreateBookingCommand
    // private UUID flightId;
    // private String flightCode; // * we're not using it
    // private BigDecimal moneyValue;
    // private Integer usedMiles;
    // private Integer totalSeats;
    // private String userId;
    // private String messageType;

    @Autowired
    private BookingRepository bookingRepository;

    public void insertBooking(CreateBookingCommand createBookingCommand) {

        String bookingCode;

        do {
            bookingCode = generateBookingcode();
        } while (bookingRepository.getBookingByCode(bookingCode) != null);

        Booking booking = Booking.builder()
                .bookingCode(null)
                .flightCode(null)
                .bookingDate(null)
                .bookingStatus(null)
                .moneySpent(null)
                .milesSpent(null)
                .numberOfSeats(null)
                .userId(bookingCode)
                .build();
    }

    public String generateBookingcode() {

        // generate 3 random uppercase letters
        StringBuilder letters = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            char randomLetter = (char) ('A' + ThreadLocalRandom.current().nextInt(26)); // Gera letra entre 'A' e 'Z'
            letters.append(randomLetter);
        }

        // generate three random numbers
        StringBuilder numbers = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            int randomNumber = ThreadLocalRandom.current().nextInt(10); // Gera número entre 0 e 9
            numbers.append(randomNumber);
        }

        // concat both
        return letters.toString() + numbers.toString();

    }

}
