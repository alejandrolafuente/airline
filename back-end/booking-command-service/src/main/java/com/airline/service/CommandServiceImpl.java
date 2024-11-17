package com.airline.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CommandServiceImpl implements CommandService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingStatusRep bookingStatusRep;

    @Autowired
    private StatusChangeRepository statusChangeRepository;

    @Override
    public void updateBookingStatus(String identifier, int statusCode) throws JsonProcessingException {

        Booking booking = identifier.length() == 36
                ? bookingRepository.findById(UUID.fromString(identifier)).orElse(null)
                : bookingRepository.getBookingByCode(identifier);

        if (booking == null) {
            throw new IllegalArgumentException("Booking Not Found");
        }

        BookingStatus initialStatus = booking.getBookingStatus();

        // Atualiza a reserva com o novo status
        booking.setBookingStatus(bookingStatusRep.findByStatusCode(statusCode));
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
        Command commandMessage = new Command(newHistory);

        String message = objectMapper.writeValueAsString(commandMessage);
        rabbitTemplate.convertAndSend("BookingQueryRequestChannel", message);
    }

}
