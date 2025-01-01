package com.airline.sagas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.model.Employee;
import com.airline.repository.EmployeeRepository;
import com.airline.sagas.queries.ManageRegisterRes;
import com.airline.sagas.queries.VerifyEmployeeQuery;

@Service
public class SagaService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // R17
    public ManageRegisterRes verifyClient(VerifyEmployeeQuery query) {

        // 1. verfify by cpf and email both
        Employee employee = employeeRepository.findByCpfAndEmail(query.getEmployeeCpf(), query.getEmployeeEmail());

        if (employee != null) {

            return ManageRegisterRes.builder()
                    .cpf(employee.getCpf())
                    .response("Employee exists already")
                    .startSaga(false)
                    .build();

        }

        // 2. verify by cpf only
        employee = employeeRepository.getEmployeeByCpf(query.getEmployeeCpf());

        if (employee != null) {

            return ManageRegisterRes.builder()
                    .cpf(query.getEmployeeCpf())
                    .response("This CPF is registered in the system, please change")
                    .startSaga(false)
                    .build();
        }

        // 2. verify by email only
        employee = employeeRepository.getEmployeeByEmail(query.getEmployeeEmail());

        if (employee != null) {

            return ManageRegisterRes.builder()
                    .cpf(query.getEmployeeCpf())
                    .response("This email is registered in the system, please change")
                    .startSaga(false)
                    .build();
        }

        return ManageRegisterRes.builder()
                .cpf(query.getEmployeeCpf())
                .response("Must start Register Employee SAGA")
                .startSaga(true)
                .build();
    }

}
