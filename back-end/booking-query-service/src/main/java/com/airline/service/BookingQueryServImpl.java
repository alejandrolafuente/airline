package com.airline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.cqrs.commands.DoCheckInCommand;
import com.airline.dto.response.R03ResDTO;
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
    public List<R03ResDTO> findBookedFlights(String userId) {

        List<BookingQuery> bookedFlights = bookingQueryRepository.findByStatusCodeAndUserId(1, userId);

        return bookedFlights.stream().map(R03ResDTO::new).toList();
    }

    // R10: request from cqrs
    @Override
    public void doCheckIn(DoCheckInCommand doCheckInCommand) {

        BookingQuery bookingQuery = bookingQueryRepository.findByBookingCommandId(doCheckInCommand.getBookingId());

        bookingQuery.setStatusCommandId(doCheckInCommand.getFStatusCommandId());
        bookingQuery.setStatusCode(doCheckInCommand.getFStatusCode());
        bookingQuery.setStatusAcronym(doCheckInCommand.getFStatusAcronym());
        bookingQuery.setStatusDescription(doCheckInCommand.getFStatusDescription());

        bookingQuery = bookingQueryRepository.save(bookingQuery);

        StatChangHistQuery statChangHistQuery = StatChangHistQuery.builder()
                .commandChangeId(doCheckInCommand.getChangeId())
                .changeDate(doCheckInCommand.getChangeDate())
                .bookingCode(bookingQuery.getBookingCode()) // * reference from the another table
                .iStatusCommandId(doCheckInCommand.getIStatusCommandId())
                .iStatusCode(doCheckInCommand.getIStatusCode())
                .iStatusAcronym(doCheckInCommand.getIStatusAcronym())
                .iStatusDescription(doCheckInCommand.getIStatusDescription())
                .fStatusCommandId(doCheckInCommand.getFStatusCommandId())
                .fStatusCode(doCheckInCommand.getFStatusCode())
                .fStatusAcronym(doCheckInCommand.getFStatusAcronym())
                .fStatusDescription(doCheckInCommand.getFStatusDescription())
                .build();

        statChangHistQueryRep.save(statChangHistQuery);

        System.out.println("\nAPARENTEMENTE CHECK-IN FOI FEITO COM SUCESSO VERIFIQUE BANCOS DE DADOS");

    }

}
