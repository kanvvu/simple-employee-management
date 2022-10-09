package com.example.tutorial3.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import com.example.tutorial3.model.Employee;
import com.example.tutorial3.repository.EmployeeRepository;


@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public void addNewEmployee(String firstName, String lastName, String email) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employeeRepository.save(employee);
    }

    @Override
    public Boolean deleteEmployee(long id) {
        if(employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addNewEmployee(Employee employee) {
        employeeRepository.save(employee);
        
    }

    @Override
    public Employee getEmployeeByID(long id) {
        Optional<Employee> optional = employeeRepository.findById(id);
        Employee employee = null;
        if(optional.isPresent()) {
            employee = optional.get();
        } else {
            throw new RuntimeException(" Employee not found for id :: " + id);
        }

        return employee;
    }

    @Override
    public Page<Employee> findPaginated(int pageNo, int pageSize, String  sortField, String  sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.employeeRepository.findAll(pageable);
    }

    
}
