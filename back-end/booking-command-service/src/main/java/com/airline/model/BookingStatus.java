package com.airline.model;

import java.io.Serializable;
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
@Table(name = "status_table")
public class BookingStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "status_id", nullable = false, updatable = false)
    private UUID statusId;

    @Column(name = "status_code", nullable = false, unique = true)
    private Integer statusCode;

    @Column(name = "status_acronym", nullable = false, unique = true)
    private String statusAcronym;

    @Column(name = "status_description", nullable = false, unique = true)
    private String statusDescription; // ""
}
