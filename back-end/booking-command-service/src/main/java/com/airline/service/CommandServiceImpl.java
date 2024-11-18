package com.airline.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airline.cqrs.Command;
import com.airline.dto.R10R12ResDTO;
import com.airline.exceptions.BookingNotFoundException;
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
    @Transactional
    public R10R12ResDTO updateBookingStatus(String identifier, int statusCode) throws JsonProcessingException {

        // 1. finds booking
        Booking booking = identifier.length() == 36
                ? bookingRepository.findById(UUID.fromString(identifier)).orElseThrow(
                        () -> new BookingNotFoundException("Booking not found for ID: " + identifier))
                : bookingRepository.getBookingByCode(identifier).orElseThrow(
                        () -> new BookingNotFoundException("Booking not found for CODE: " + identifier));

        BookingStatus initialStatus = booking.getBookingStatus();

        // 2. Atualiza a reserva com o novo status
        booking.setBookingStatus(bookingStatusRep.findByStatusCode(statusCode));
        booking = bookingRepository.save(booking);

        BookingStatus finalStatus = booking.getBookingStatus();

        // 3. Cria registro de histórico
        StatusChangeHist newHistory = StatusChangeHist.builder()
                .changeDate(ZonedDateTime.now(ZoneId.of("UTC")))
                .booking(booking)
                .initialStatus(initialStatus)
                .finalStatus(finalStatus)
                .build();

        newHistory = statusChangeRepository.save(newHistory);

        // Prepara a mensagem para o serviço de consulta e envia
        Command commandMessage = new Command(newHistory);

        String message = objectMapper.writeValueAsString(commandMessage);
        rabbitTemplate.convertAndSend("BookingQueryRequestChannel", message);

        // dto de resposta para o cliente
        R10R12ResDTO dto = R10R12ResDTO.builder()
                .bookingId(newHistory.getBooking().getBookingId())
                .bookingCode(newHistory.getBooking().getFlightCode())
                .initialStatus(newHistory.getInitialStatus().getStatusDescription())
                .currentStatus(newHistory.getFinalStatus().getStatusDescription())
                .build();

        return dto;
    }

}
