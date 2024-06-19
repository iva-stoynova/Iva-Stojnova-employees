package app.services;

import app.model.Employee;
import app.model.PairOfEmployees;

import java.util.List;

public interface EmployeeService {

    void addEmployeeRecords(List<Employee> records);

    List<PairOfEmployees> findAllPairOfEmplsWithOverlap();
}
