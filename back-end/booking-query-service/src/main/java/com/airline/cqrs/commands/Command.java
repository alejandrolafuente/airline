package com.airline.cqrs.commands;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Command {
    private UUID bookingId; // bookingCommandId
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
}
