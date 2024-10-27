package com.airline.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airline.model.StatusChangeHist;

@Repository
public interface StatusChangeRepository extends JpaRepository<StatusChangeHist, UUID> {

}
