package com.airline.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airline.model.StatChangHistQuery;

@Repository
public interface StatChangHistQueryRep extends JpaRepository<StatChangHistQuery, UUID> {

}
