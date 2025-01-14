package com.airline.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airline.dto.response.R16ResDTO;
import com.airline.service.EmpService;

@RestController
@CrossOrigin
@RequestMapping("employee")
public class EmpController {

    @Autowired
    private EmpService empService;

    @GetMapping("/employees")
    public ResponseEntity<List<R16ResDTO>> getActiveEmployees() {
        List<R16ResDTO> dto = empService.getActiveEmployees();
        return ResponseEntity.ok().body(dto);
    }

}
