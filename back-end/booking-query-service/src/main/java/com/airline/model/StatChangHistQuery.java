package com.airline.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "change_query_table")
public class StatChangHistQuery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "change_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "command_change_id", nullable = false)
    private UUID commandChangeId; // * id in booking command service

    @Column(name = "change_date", nullable = false)
    private ZonedDateTime changeDate;

    @Column(name = "booking_code", nullable = false)
    private String bookingCode;

    // initial status
    @Column(name = "i_status_command_id", nullable = false)
    private UUID iStatusCommandId; // * id in booking command service

    @Column(name = "i_status_code", nullable = false)
    private Integer iStatusCode;

    @Column(name = "i_status_acronym", nullable = false)
    private String iStatusAcronym;

    @Column(name = "i_status_description", nullable = false)
    private String iStatusDescription;

    // final status
    @Column(name = "f_status_command_id", nullable = false)
    private UUID fStatusCommandId; // * id in booking command service

    @Column(name = "f_status_code", nullable = false)
    private Integer fStatusCode;

    @Column(name = "f_status_acronym", nullable = false)
    private String fStatusAcronym;

    @Column(name = "f_status_description", nullable = false)
    private String fStatusDescription;

}
