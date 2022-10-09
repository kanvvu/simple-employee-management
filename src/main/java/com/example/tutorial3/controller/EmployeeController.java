package com.example.tutorial3.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String addEmployee(@ModelAttribute("employee") Employee employee, RedirectAttributes redirAttrs) {
        employeeService.addNewEmployee(employee);
        redirAttrs.addFlashAttribute("success", "Adding employee went successfully!");
        return "redirect:/";
    }

    @GetMapping(path="/")
    public String viewIndex(Model model) {
        return findPaginated(1, "firstName", "asc", model);
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
    public String removeEmployee(@PathVariable("id") long id, RedirectAttributes redirAttrs) {
        employeeService.deleteEmployee(id);
        redirAttrs.addFlashAttribute("success", "Deleting employee went successfully!");
        return "redirect:/";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable ("pageNo") int pageNo, @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 5;

        Page<Employee> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Employee> listEmployees = page.getContent();
        List<Employee> modifable = new ArrayList<Employee>(listEmployees);

        if(sortField.equals("firstName")) {
            if(sortDir.equals("asc")) {
                modifable.sort(Comparator.comparing(Employee::getFirstName));
            } else {
                modifable.sort(Comparator.comparing(Employee::getFirstName).reversed());
            }
        }

        if(sortField.equals("lastName")) {
            if(sortDir.equals("asc")) {
                modifable.sort(Comparator.comparing(Employee::getLastName));
            } else {
                modifable.sort(Comparator.comparing(Employee::getLastName).reversed());
            }
        }

        if(sortField.equals("email")) {
            if(sortDir.equals("asc")) {
                modifable.sort(Comparator.comparing(Employee::getEmail));
            } else {
                modifable.sort(Comparator.comparing(Employee::getEmail).reversed());
            }
        }

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listEmployees", modifable);
        return "index";
    }
    
}
