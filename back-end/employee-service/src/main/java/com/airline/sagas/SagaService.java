package com.airline.sagas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.model.Employee;
import com.airline.repository.EmployeeRepository;
import com.airline.sagas.commands.CreateEmployeeCommand;
import com.airline.sagas.commands.DeleteEmpCommand;
import com.airline.sagas.commands.UpdateEmployeeCommand;
import com.airline.sagas.events.EmpDeletedEvent;
import com.airline.sagas.events.EmpUpdatedEvent;
import com.airline.sagas.events.EmployeeCreatedEvent;
import com.airline.sagas.queries.ManageRegisterRes;
import com.airline.sagas.queries.VerifyEmployeeQuery;

@Service
public class SagaService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // R17 - 1
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

    // R17 - 2
    public EmployeeCreatedEvent saveNewEmployee(CreateEmployeeCommand command) {

        Employee employee = Employee.builder()
                .userId(command.getUserID())
                .name(command.getName())
                .cpf(command.getCpf())
                .email(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .status("ACTIVE ")
                .build();

        employee = employeeRepository.save(employee);

        EmployeeCreatedEvent event = EmployeeCreatedEvent.builder()
                .email(employee.getEmail())
                .messageType("EmployeeCreatedEvent")
                .build();

        return event;
    }

    // R18
    public EmpUpdatedEvent updateEmployee(UpdateEmployeeCommand command) {

        EmpUpdatedEvent event = new EmpUpdatedEvent();

        Employee employee = employeeRepository.findByUserId(command.getUserID());

        if ((employee.getName() != command.getName()) || (employee.getEmail() != command.getEmail())) {
            event.setProceedSaga(true);
        } else {
            event.setProceedSaga(false);
        }

        employee.setName(command.getName());
        employee.setEmail(command.getEmail());
        employee.setPhoneNumber(command.getPhoneNumber());

        employee = employeeRepository.save(employee);

        event.setName(employee.getName());
        event.setEmail(employee.getEmail());
        event.setMessagetype("EmpUpdatedEvent");

        return event;
    }

    // R19
    public EmpDeletedEvent deleteEmployee(DeleteEmpCommand command) {

        Employee employee = employeeRepository.findByUserId(command.getUserId());

        employee.setStatus(command.getUserStatus());

        employee = employeeRepository.save(employee);

        EmpDeletedEvent event = EmpDeletedEvent.builder()
                .userId(employee.getUserId())
                .name(employee.getName())
                .employeeStatus(employee.getStatus())
                .messageType("EmpDeletedEvent")
                .build();

        return event;
    }

}
