package com.airline.sagas;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.cqrs.Command;
import com.airline.model.Booking;
import com.airline.model.BookingStatus;
import com.airline.model.StatusChangeHist;
import com.airline.repository.BookingRepository;
import com.airline.repository.BookingStatusRep;
import com.airline.repository.StatusChangeRepository;
import com.airline.sagas.commands.BookingCommand;
import com.airline.sagas.commands.CancelBookingByIdCommand;
import com.airline.sagas.commands.CancelBookingsCommand;
import com.airline.sagas.commands.CompleteBookingsCommand;
import com.airline.sagas.commands.CreateBookingCommand;
import com.airline.sagas.events.BookingCanByIdEvent;
import com.airline.sagas.events.BookingCancelledEvent;
import com.airline.sagas.events.BookingCreatedEvent;
import com.airline.sagas.events.BookingsCompletedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class SagaService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingStatusRep bookingStatusRep;

    @Autowired
    private StatusChangeRepository statusChangeRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // R07
    @Transactional
    public BookingCreatedEvent insertBooking(CreateBookingCommand command) throws JsonProcessingException {

        if (command.getMoneyValue() == null) {
            command.setMoneyValue(BigDecimal.ZERO);
        }

        if (command.getUsedMiles() == null) {
            command.setUsedMiles(0);
        }

        String bookingCode;

        do {
            bookingCode = generateBookingcode();
        } while (bookingRepository.getBookingByCode(bookingCode) != null);

        Booking booking = Booking.builder()
                .bookingCode(bookingCode)
                .flightCode(command.getFlightCode())
                .bookingDate(ZonedDateTime.now(ZoneId.of("UTC")))
                .bookingStatus(bookingStatusRep.findByStatusCode(1))
                .moneySpent(command.getMoneyValue())
                .milesSpent(command.getUsedMiles())
                .numberOfSeats(command.getTotalSeats())
                .userId(command.getUserId())
                .transactionId(command.getTransactionId())
                .build();

        booking = bookingRepository.save(booking);

        // send message to booking query service:

        BookingCommand bookingCommand = new BookingCommand(booking);

        var message = objectMapper.writeValueAsString(bookingCommand);

        rabbitTemplate.convertAndSend("BookingQueryRequestChannel", message);

        // send return back to sagas service:

        BookingCreatedEvent bookingCreatedEvent = BookingCreatedEvent.builder()
                .bookingStatus(booking.getBookingStatus().getStatusDescription())
                .bookingCode(booking.getBookingCode())
                .bookingDate(booking.getBookingDate()) // UTC local
                .messageType("BookingCreatedEvent")
                .build();

        return bookingCreatedEvent;

    }

    // R08 - 1
    @Transactional
    public BookingCanByIdEvent cancelSingleBooking(CancelBookingByIdCommand command) throws JsonProcessingException {

        Booking booking = bookingRepository.findById(command.getBookingId()).orElseThrow(null);

        // muda de status de 'BOOKED' para 'CANCELLED'
        BookingStatus initialStatus = booking.getBookingStatus();

        // Atualiza a reserva com o status "CANCELLED"
        booking.setBookingStatus(bookingStatusRep.findByStatusCode(3));

        booking = bookingRepository.save(booking);

        // Cria registro de histórico
        BookingStatus finalStatus = booking.getBookingStatus();

        StatusChangeHist newHistory = StatusChangeHist.builder()
                .changeDate(ZonedDateTime.now(ZoneId.of("UTC")))
                .booking(booking)
                .initialStatus(initialStatus)
                .finalStatus(finalStatus)
                .build();

        newHistory = statusChangeRepository.save(newHistory);

        // Prepara a mensagem para o serviço de consulta:
        Command commandMessage = new Command(newHistory);

        String message = objectMapper.writeValueAsString(commandMessage);

        rabbitTemplate.convertAndSend("BookingQueryRequestChannel", message);

        // prepara o retorno para sagas
        BookingCanByIdEvent event = BookingCanByIdEvent.builder()
                .flightCode(booking.getFlightCode())
                .moneySpent(booking.getMoneySpent())
                .milesSpent(booking.getMilesSpent())
                .numberOfSeats(booking.getNumberOfSeats())
                .userId(booking.getUserId())
                .messageType("BookingCanByIdEvent")
                .build();

        return event;
    }

    // R13 - 2
    @Transactional
    public BookingCancelledEvent cancelBookings(CancelBookingsCommand cancelBookingCommand)
            throws JsonProcessingException {

        List<Booking> relatedBookings = bookingRepository.findByFlightCode(cancelBookingCommand.getFlightCode());

        for (Booking booking : relatedBookings) {

            BookingStatus initialStatus = booking.getBookingStatus();

            // Atualiza a reserva com o status "CANCELLED"
            booking.setBookingStatus(bookingStatusRep.findByStatusCode(3));

            booking = bookingRepository.save(booking);

            // Cria registro de histórico
            BookingStatus finalStatus = booking.getBookingStatus();

            StatusChangeHist newHistory = StatusChangeHist.builder()
                    .changeDate(ZonedDateTime.now(ZoneId.of("UTC")))
                    .booking(booking)
                    .initialStatus(initialStatus)
                    .finalStatus(finalStatus)
                    .build();

            newHistory = statusChangeRepository.save(newHistory);

            // Prepara a mensagem para o serviço de consulta:
            Command command = new Command(newHistory);

            String message = objectMapper.writeValueAsString(command);
            rabbitTemplate.convertAndSend("BookingQueryRequestChannel", message);

            // prepara o retorno para sagas, o cliente que precisa ressarcir:
            BookingCancelledEvent bookingCancelledEvent = BookingCancelledEvent.builder()
                    .userId(booking.getUserId())
                    .refundMoney(booking.getMoneySpent())
                    .refundMiles(booking.getMilesSpent())
                    .messageType("BookingCancelledEvent")
                    .build();

            return bookingCancelledEvent;

        }

        return null;
    }

    // R14 - Realização do Voo
    @Transactional
    public BookingsCompletedEvent completeBookings(CompleteBookingsCommand completeBookingCommand)
            throws JsonProcessingException {

        List<Booking> relatedBookings = bookingRepository.findByFlightCode(completeBookingCommand.getFlightCode());

        for (Booking booking : relatedBookings) {

            BookingStatus initialStatus = booking.getBookingStatus();

            // Atualiza a reserva com o status "COMPLETED" ou 'NOT COMPLETED'

            // **TERMINAR ESTE PASSP COM OS CONDICIONAIS ABAIXO COMENTADOS:

            // if ("BOARDED".equals(booking.getBookingStatus().getStatusDescription())) {
            // booking.setBookingStatus(bookingStatusRep.findByStatusCode(5));
            // } else if () {

            // }

            booking.setBookingStatus(bookingStatusRep.findByStatusCode(5));

            booking = bookingRepository.save(booking);

            // Cria registro de histórico
            BookingStatus finalStatus = booking.getBookingStatus();

            StatusChangeHist newHistory = StatusChangeHist.builder()
                    .changeDate(ZonedDateTime.now(ZoneId.of("UTC")))
                    .booking(booking)
                    .initialStatus(initialStatus)
                    .finalStatus(finalStatus)
                    .build();

            newHistory = statusChangeRepository.save(newHistory);

            // Prepara a mensagem para o serviço de consulta
            Command command = new Command(newHistory);

            String message = objectMapper.writeValueAsString(command);
            rabbitTemplate.convertAndSend("BookingQueryRequestChannel", message);

        }

        BookingsCompletedEvent event = BookingsCompletedEvent.builder()
                .flightCode(completeBookingCommand.getFlightCode())
                .messageType("BookingsCompletedEvent")
                .build();

        return event;
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
