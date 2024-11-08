package com.airline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.cqrs.commands.Command;
import com.airline.dto.response.R03ResDTO;
import com.airline.dto.response.R04ResDTO;
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

    // R03
    @Override
    public List<R03ResDTO> findClientBookings(String userId) {

        List<BookingQuery> bookedFlights = bookingQueryRepository.findByUserId(userId);

        return bookedFlights.stream().map(R03ResDTO::new).toList();
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

    // R04
    @Override
    public R04ResDTO getBooking(String bookingId) {

        
        return null;
    }

}
