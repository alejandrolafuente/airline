package com.airline.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.cqrs.DoCheckInCommand;
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
    public void doCheckIn(UUID bookingId) throws JsonProcessingException {

        Booking booking = bookingRepository.findById(bookingId).orElse(null);

        BookingStatus initialStatus = booking.getBookingStatus();

        // atualiza reserva
        booking.setBookingStatus(bookingStatusRep.findByStatusCode(2));

        booking = bookingRepository.save(booking);

        // cria registro de historico
        BookingStatus finalStatus = booking.getBookingStatus();

        StatusChangeHist newHistory = StatusChangeHist.builder()
                .changeDate(ZonedDateTime.now(ZoneId.of("UTC")))
                .booking(booking)
                .initialStatus(initialStatus)
                .finalStatus(finalStatus)
                .build();

        newHistory = statusChangeRepository.save(newHistory);

        // prepara mensagem e envia para query service
        DoCheckInCommand doCheckInCommand = DoCheckInCommand.builder()
                .bookingId(newHistory.getBooking().getBookingId().toString())
                .changeId(newHistory.getId().toString())
                .changeDate(newHistory.getChangeDate())
                .iStatusCommandId(newHistory.getInitialStatus().getStatusId().toString())
                .iStatusCode(newHistory.getInitialStatus().getStatusCode())
                .iStatusAcronym(newHistory.getInitialStatus().getStatusAcronym())
                .iStatusDescription(newHistory.getInitialStatus().getStatusDescription())
                .fStatusCommandId(newHistory.getFinalStatus().getStatusId().toString())
                .fStatusCode(newHistory.getFinalStatus().getStatusCode())
                .fStatusAcronym(newHistory.getFinalStatus().getStatusAcronym())
                .fStatusDescription(newHistory.getFinalStatus().getStatusDescription())
                .messageType("DoCheckInCommand")
                .build();

        var message = objectMapper.writeValueAsString(doCheckInCommand);

        rabbitTemplate.convertAndSend("BookingQueryRequestChannel", message);

    }

}
