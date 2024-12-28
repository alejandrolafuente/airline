package com.airline.model;

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
@Table(name = "employee_table")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employee_id", nullable = false, unique = true, updatable = false)
    private UUID employeeId;
    @Column(name = "user_id", nullable = false, unique = true, updatable = false)
    private String userId;
    @Column(name = "name", nullable = false, unique = false, updatable = true)
    private String name;
    @Column(name = "cpf", nullable = false, unique = true, updatable = false)
    private String cpf;
    @Column(name = "email", nullable = false, unique = true, updatable = true)
    private String email;
    @Column(name = "phone_number", nullable = false, unique = true, updatable = true)
    private String phoneNumber;
    @Column(name = "status", nullable = false, unique = true, updatable = true)
    private String status;
}
