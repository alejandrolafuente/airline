package com.airline.cqrs;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.airline.model.StatusChangeHist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Command {
    private UUID bookingId;
    private UUID changeId;
    private ZonedDateTime changeDate;
    private UUID iStatusCommandId;
    private Integer iStatusCode;
    private String iStatusAcronym;
    private String iStatusDescription;
    private UUID fStatusCommandId;
    private Integer fStatusCode;
    private String fStatusAcronym;
    private String fStatusDescription;
    private String messageType;

    public Command(StatusChangeHist newHistory) {
        bookingId = newHistory.getBooking().getBookingId();
        changeId = newHistory.getId();
        changeDate = newHistory.getChangeDate();
        iStatusCommandId = newHistory.getInitialStatus().getStatusId();
        iStatusCode = newHistory.getInitialStatus().getStatusCode();
        iStatusAcronym = newHistory.getInitialStatus().getStatusAcronym();
        iStatusDescription = newHistory.getInitialStatus().getStatusDescription();
        fStatusCommandId = newHistory.getFinalStatus().getStatusId();
        fStatusCode = newHistory.getFinalStatus().getStatusCode();
        fStatusAcronym = newHistory.getFinalStatus().getStatusAcronym();
        fStatusDescription = newHistory.getFinalStatus().getStatusDescription();
        messageType = "SynCommand";
    }
}
