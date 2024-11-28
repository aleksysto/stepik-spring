package com.example.demo.controller;
import com.example.demo.domain.Person;
import com.example.demo.service.CsvService;
import com.example.demo.service.FileService;
import com.example.demo.service.PersonService;
import com.example.demo.validation.UniqueEmailValidator;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class PersonViewController {
    private final PersonService personService;
    private final CsvService csvService;
    private final FileService fileService;

    @Autowired
    public PersonViewController(PersonService personService, CsvService csvService, FileService fileService) {
        this.personService = personService;
        this.csvService = csvService;
        this.fileService = fileService;
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
    public String editEmployee(@PathVariable int id, @Valid @ModelAttribute("person") Person employee, BindingResult result, RedirectAttributes redirectAttributes, Model model, MultipartFile image) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            model.addAttribute("person", employee);
            return "employees/editForm";
        }
        try{
            System.out.println(employee);
            employee.setCompanyName(employee.getEmail().split("@")[1].split("\\.")[0]);
            if (!image.isEmpty()){
                String filepath = fileService.saveFile(image);
                employee.setPhoto(image);
                employee.setImagePath(filepath);
            }
            personService.updatePerson(id, employee);
            redirectAttributes.addFlashAttribute("successMessage", "employee edited");
        } catch (Exception e){
            System.out.println(e);
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
    public String addEmployee (@Valid @ModelAttribute("person") Person employee,BindingResult result, RedirectAttributes redirectAttributes,  Model model, @RequestParam MultipartFile image) {
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
            if (!image.isEmpty()){
                String filepath = fileService.saveFile(image);
                employee.setPhoto(image);
                employee.setImagePath(filepath);
            }
            personService.addPerson(employee);
            redirectAttributes.addFlashAttribute("successMessage", "employee added");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Couldn't add employee");
            redirectAttributes.addFlashAttribute("employee", employee);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.person", result);
        }
        return "redirect:list";
    }
    @PostMapping("/get-csv")
    public ResponseEntity<Resource> exportEmployeesToCSV(@RequestParam List<String> columns) {
        try {
            String csvContent = csvService.exportToCSV(columns, personService.getAllWorkers());
            ByteArrayResource resource = new ByteArrayResource(csvContent.getBytes(StandardCharsets.UTF_8));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"employees.csv\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
            return ResponseEntity.ok().headers(headers).body(resource);
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/get-zip")
    public ResponseEntity<Resource> exportPhotos(@RequestParam (value = "format", required = false) String format) {
        try {
            // Generate the ZIP file and return its path
            String f = "[id]_[surname]";
            Path zipFilePath = fileService.exportPhotosToZip(f, personService.getAllWorkers());

            // Load the ZIP file as a Resource
            Resource zipResource = fileService.loadFileAsResource(zipFilePath.getFileName().toString());

            // Set headers to indicate a file download
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"employee_photos.zip\"")
                    .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                    .body(zipResource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping("/import")
    public String uploadCsvWithEmployees(
            @RequestParam("csvFile") MultipartFile csvFile,
            RedirectAttributes redirectAttributes) {
        try {
            // Ensure the CSV file is not empty
            if (csvFile.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "No CSV file provided");
                return "redirect:/employees";
            }

            // Save the CSV file

            // Reload employees from the uploaded CSV
            List<Person> added = csvService.importFromCSV(csvFile, personService);
            personService.addPersons(added);

            //if (!personService.getValidationErrors().isEmpty()) {
            //    redirectAttributes.addFlashAttribute("errorMessage", "Importing employees failed due to errors in CSV file format");
            //    return "redirect:/employees/validation-errors";
            //}

            redirectAttributes.addFlashAttribute("successMessage", "Employees and images have been imported successfully");
        } catch (Exception e) {
            e.printStackTrace(); // Log the actual error
            redirectAttributes.addFlashAttribute("errorMessage", "Importing employees failed: " + e.getMessage());
        }
        return "redirect:/employees/list";
    }

    @GetMapping("/image/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        Resource file = fileService.loadFileAsResource(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
