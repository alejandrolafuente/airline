package com.airline.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.cqrs.commands.Command;
import com.airline.dto.response.R03ResDTO;
import com.airline.dto.response.R04ResDTO;
import com.airline.exceptions.BookingNotFoundException;
import com.airline.exceptions.UserBookingsNotFoundException;
import com.airline.model.BookingQuery;
import com.airline.model.StatChangHistQuery;
import com.airline.repository.BookingQueryRepository;
import com.airline.repository.StatChangHistQueryRep;

@Service
public class BookingQueryServImpl implements BookingQueryService {

    @Autowired
    private BookingQueryRepository bookingQueryRepository;

    @Autowired
    private StatChangHistQueryRep statChangHistQueryRep;

    // R03 - 2
    @Override
    public List<R03ResDTO> findClientBookings(String userId) {

        List<BookingQuery> clientBookings = bookingQueryRepository.findByUserId(userId)
                .filter(bookings -> !bookings.isEmpty()) // Verifica se a lista nao esta vazia
                .orElseThrow(() -> new UserBookingsNotFoundException("Bookings not found for user: " + userId));

        return clientBookings.stream().map(R03ResDTO::new).toList();
    }

    // R04
    @Override
    public R04ResDTO getBooking(String bookingId) {

        BookingQuery booking = bookingQueryRepository.findById(UUID.fromString(bookingId)).orElseThrow(

                () -> new BookingNotFoundException("Booking not found for ID: " + bookingId));

        return new R04ResDTO(booking);
    }

    // R06 - 2
    @Override
    public List<String> getFlightCodes(List<UUID> transactionIds) {

        List<String> listR06ResDTO = new ArrayList<>();

        for (UUID uuid : transactionIds) {

            BookingQuery bookingQuery = bookingQueryRepository.findByTransactionId(uuid).orElseThrow(null);

            String flightCode = bookingQuery.getFlightCode();

            listR06ResDTO.add(flightCode);

        }

        return listR06ResDTO;
    }

    // R10, R12: requests from cqrs
    // R14: request from saga
    @Override
    public void syncronizeDBs(Command command) {

        BookingQuery bookingQuery = bookingQueryRepository.findByBookingCommandId(command.getBookingId());

        bookingQuery.setStatusCommandId(command.getFStatusCommandId());
        bookingQuery.setStatusCode(command.getFStatusCode());
        bookingQuery.setStatusAcronym(command.getFStatusAcronym());
        bookingQuery.setStatusDescription(command.getFStatusDescription());

        bookingQuery = bookingQueryRepository.save(bookingQuery);

        StatChangHistQuery statChangHistQuery = StatChangHistQuery.builder()
                .commandChangeId(command.getChangeId())
                .changeDate(command.getChangeDate())
                .bookingCode(bookingQuery.getBookingCode()) // * reference from the another table
                .iStatusCommandId(command.getIStatusCommandId())
                .iStatusCode(command.getIStatusCode())
                .iStatusAcronym(command.getIStatusAcronym())
                .iStatusDescription(command.getIStatusDescription())
                .fStatusCommandId(command.getFStatusCommandId())
                .fStatusCode(command.getFStatusCode())
                .fStatusAcronym(command.getFStatusAcronym())
                .fStatusDescription(command.getFStatusDescription())
                .build();

        statChangHistQueryRep.save(statChangHistQuery);

        System.out.println("\nAPARENTEMENTE ATUALIZAÇÃO FOI BEM SUCEDIDA, VERIFIQUE BANCOS DE DADOS");

    }

}
