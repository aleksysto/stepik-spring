package com.example.demo.controller;
import com.example.demo.domain.Person;
import com.example.demo.service.PersonService;
import com.example.demo.validation.UniqueEmailValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class PersonViewController {
    private final PersonService personService;

    @Autowired
    public PersonViewController(PersonService personService) {
        this.personService = personService;
    }

    @ModelAttribute("totalEmployees")
    public int populateEmployeeCount() {
        return personService.getAllWorkers().size();
    }

    @ModelAttribute("employeeCountries")
    public List<String> populateEmployeeCountries() {
        return personService.getEmployeeCountries();
    }
    @GetMapping("/home")
    public String welcomePage(Model model) {
        model.addAttribute("message", "Hello from Thymeleaf!");
        return "index";
    }


    @GetMapping("/list")
    public String listEmployees(@RequestParam(value = "country", required = false) String country, Model model) {
        List<Person> employees;
        if (country != null && !country.isEmpty()) {
            employees = personService.getEmployeesFromCountry(country);
        } else {
            employees = personService.getAllWorkers();
        }
        model.addAttribute("employees", employees);
        model.addAttribute("country", country);
        model.addAttribute("currencySummary", personService.getCurrencySalaryPairs());
        return "employees/list";
    }

    @GetMapping("/details/{id}")
    public String employeeDetails(@PathVariable int id, Model model) {
        Optional<Person> employee = personService.getPersonById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
        } else {
            model.addAttribute("error", "Employee not found");
            return "employees/error";
        }
        return "employees/details";
    }
    @GetMapping("/edit/{id}")
    public String editEmployee(@PathVariable int id, Model model) {
        Optional<Person> employee = personService.getPersonById(id);
        if (employee.isPresent()) {
            model.addAttribute("person", employee.get());
        } else {
            model.addAttribute("error", "Employee not found");
            return "employees/error";
        }
        return "employees/editForm";
    }
    @PostMapping("/edit/{id}")
    public String editEmployee(@PathVariable int id, @Valid @ModelAttribute("person") Person employee, BindingResult result, RedirectAttributes redirectAttributes, Model model ) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            model.addAttribute("person", employee);
            return "employees/editForm";
        }
        try{
            System.out.println(employee);
            employee.setCompanyName(employee.getEmail().split("@")[1].split("\\.")[0]);
            personService.updatePerson(id, employee);
            redirectAttributes.addFlashAttribute("successMessage", "employee edited");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Couldn't edit employee");
            redirectAttributes.addFlashAttribute("employee", employee);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.person", result);
        }
        return "redirect:/employees/list";
    }
    @GetMapping("/add")
    public String addEmployee(Model model) {
        if (!model.containsAttribute("person")) {
            model.addAttribute("person", new Person());
        }
        return "employees/addForm";
    }
    @PostMapping("/add")
    public String addEmployee (@Valid @ModelAttribute("person") Person employee,BindingResult result, RedirectAttributes redirectAttributes,  Model model) {
        if (personService.existsByEmail(employee.getEmail())) {
            result.rejectValue( "email", "email.unique", "email must be unique");
        }
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            model.addAttribute("person", employee);
            return "employees/addForm";
        }
        try{
            System.out.println(employee);
            employee.setId(personService.getNewId());
            employee.setCompanyName(employee.getEmail().split("@")[1].split("\\.")[0]);
            personService.addPerson(employee);
            redirectAttributes.addFlashAttribute("successMessage", "employee added");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Couldn't add employee");
            redirectAttributes.addFlashAttribute("employee", employee);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.person", result);
        }
        return "redirect:list";
    }
}
