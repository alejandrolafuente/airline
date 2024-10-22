package com.airline.sagas;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.model.Booking;
import com.airline.repository.BookingRepository;
import com.airline.repository.BookingStatusRep;
import com.airline.sagas.commands.CreateBookingCommand;
import com.airline.sagas.events.BookingCreatedEvent;

@Service
public class SagaService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingStatusRep bookingStatusRep;

    public BookingCreatedEvent insertBooking(CreateBookingCommand createBookingCommand) {

        String bookingCode;

        do {
            bookingCode = generateBookingcode();
        } while (bookingRepository.getBookingByCode(bookingCode) != null);

        Booking booking = Booking.builder()
                .bookingCode(bookingCode)
                .flightCode(createBookingCommand.getFlightCode())
                .bookingDate(ZonedDateTime.now(ZoneId.of("UTC")))
                .bookingStatus(bookingStatusRep.findByStatusCode(1))
                .moneySpent(createBookingCommand.getMoneyValue())
                .milesSpent(createBookingCommand.getUsedMiles())
                .numberOfSeats(createBookingCommand.getTotalSeats())
                .userId(bookingCode)
                .build();

        booking = bookingRepository.save(booking);

        BookingCreatedEvent bookingCreatedEvent = BookingCreatedEvent.builder()
                .bookingStatus(booking.getBookingCode())
                .bookingCode(booking.getBookingCode())
                .bookingDate(booking.getBookingDate())
                .messageType("BookingCreatedEvent")
                .build();

        return bookingCreatedEvent;

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
