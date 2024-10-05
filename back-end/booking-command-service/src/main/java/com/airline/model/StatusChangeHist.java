package com.airline.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "change_table")
public class StatusChangeHist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "change_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "change_date", nullable = false)
    private ZonedDateTime changeDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking", referencedColumnName = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "initial_status", referencedColumnName = "status_id", nullable = false)
    private BookingStatus initialStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "final_status", referencedColumnName = "status_id", nullable = false)
    private BookingStatus finalStatus;

}
