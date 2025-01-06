package com.airline.service;

import java.util.List;

import com.airline.dto.response.R16ResDTO;

public interface EmpService {

    // R16
    List<R16ResDTO> getActiveEmployees();

}
