package com.airline.cqrs.commands;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoCheckInCommand {
    private String bookingId; // bookingCommandId
    private String changeId;
    private ZonedDateTime changeDate;
    private String iStatusCommandId;
    private Integer iStatusCode;
    private String iStatusAcronym;
    private String iStatusDescription;
    private String fStatusCommandId;
    private Integer fStatusCode;
    private String fStatusAcronym;
    private String fStatusDescription;
    private String messageType;
}
