package com.airline.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.dto.response.R16ResDTO;
import com.airline.model.Employee;
import com.airline.repository.EmployeeRepository;

@Service
public class EmpServImpl implements EmpService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<R16ResDTO> getActiveEmployees() {

        List<R16ResDTO> dto = new ArrayList<>();

        List<Employee> employeeList = employeeRepository.getActiveEmpoyees();

        for (Employee employee : employeeList) {

            R16ResDTO empDto = R16ResDTO.builder()
                    .id(employee.getEmployeeId())
                    .userId(employee.getUserId())
                    .name(employee.getName())
                    .cpf(employee.getCpf())
                    .email(employee.getEmail())
                    .phoneNumber(this.formatPhoneNumber(employee.getPhoneNumber()))
                    .build();

            dto.add(empDto);
        }

        return dto;
    }

    public String formatPhoneNumber(String phoneNumber) {

        // 41991932059
        return String.format("(%s) %s-%s",
                phoneNumber.substring(0, 2),
                phoneNumber.substring(2, 7),
                phoneNumber.substring(7));
    }

}
