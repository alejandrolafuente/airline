package com.airline.sagas;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.model.Booking;
import com.airline.repository.BookingRepository;
import com.airline.repository.BookingStatusRep;
import com.airline.sagas.commands.BookingCommand;
import com.airline.sagas.commands.CreateBookingCommand;
import com.airline.sagas.events.BookingCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SagaService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingStatusRep bookingStatusRep;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public BookingCreatedEvent insertBooking(CreateBookingCommand createBookingCommand) throws JsonProcessingException {

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
                .userId(createBookingCommand.getUserId())
                .build();

        booking = bookingRepository.save(booking);

        // send message to booking query service:

        BookingCommand bookingCommand = BookingCommand.builder()
                .bookingCommandId(booking.getBookingId())
                .bookingCode(booking.getBookingCode())
                .flightCode(booking.getFlightCode())
                .bookingDate(booking.getBookingDate())
                .statusCommandId(booking.getBookingStatus().getStatusId())
                .statusCode(booking.getBookingStatus().getStatusCode())
                .statusAcronym(booking.getBookingStatus().getStatusAcronym())
                .statusDescription(booking.getBookingStatus().getStatusDescription())
                .moneySpent(booking.getMoneySpent())
                .milesSpent(booking.getMilesSpent())
                .numberOfSeats(booking.getNumberOfSeats())
                .userId(booking.getUserId())
                .messageType("BookingCommand")
                .build();

        var message = objectMapper.writeValueAsString(bookingCommand);

        rabbitTemplate.convertAndSend("BookingQueryRequestChannel", message);

        // send return back to handler and to sagas service:

        BookingCreatedEvent bookingCreatedEvent = BookingCreatedEvent.builder()
                .bookingStatus(booking.getBookingCode())
                .bookingCode(booking.getBookingCode())
                .bookingDate(booking.getBookingDate()) // UTC local
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
