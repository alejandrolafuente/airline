package com.airline.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.airline.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Employee findByCpfAndEmail(String cpf, String email);

    @Query(value = "SELECT * FROM employee_table e WHERE e.cpf = ?1", nativeQuery = true)
    Employee getEmployeeByCpf(String cpf);

    // native query with native param
    @Query(value = "SELECT * FROM employee_table e WHERE e.email = :email", nativeQuery = true)
    Employee getEmployeeByEmail(@Param("email") String email);

    Employee findByUserId(String userId);

    @Query(value = "SELECT * FROM employee_table e WHERE e.status = 'ACTIVE' ORDER BY e.name", nativeQuery = true)
    List<Employee> getActiveEmpoyees();
}
