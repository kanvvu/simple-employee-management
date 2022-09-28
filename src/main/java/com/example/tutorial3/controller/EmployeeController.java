package com.example.tutorial3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.tutorial3.model.Employee;
import com.example.tutorial3.service.EmployeeService;

@Controller
@RequestMapping(path="/")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
  
    @PostMapping(path = "/add")
    public @ResponseBody String addNewEmployee(@RequestParam String firstName,@RequestParam String lastName, @RequestParam String email) {
        employeeService.addNewEmployee(firstName, lastName, email);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping("/addEmployee")
    public String addEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.addNewEmployee(employee);
        return "redirect:/";
    }

    @GetMapping(path="/")
    public String viewIndex(Model model) {
        model.addAttribute("listEmployees", employeeService.getAllEmployees());
        return "index";

    }

    @GetMapping(path="/addNewEmployeeForm")
    public String addNewEmployeeForm(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "addNewEmployee";
    }

    @GetMapping(path="/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {
        Employee employee = employeeService.getEmployeeByID(id);
        model.addAttribute("employee", employee);
        return "updateEmployee";

    }

    @GetMapping(path="/delete/{id}")
    public String removeEmployee(@PathVariable("id") long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/";
    }
    
}
