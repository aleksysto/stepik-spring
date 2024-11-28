package com.example.demo.service;

import com.example.demo.domain.Company;
import com.example.demo.domain.Person;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;


@Service
public class CsvService {

    public List<Person> readCsvFile(String fileName) {
        List<Person> people = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileName)))) {
            String line;
            boolean isFirstLine = true;
            int currId = 0;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");

                Person person = new Person(data[0].toString(),
                (data[1].toString()),
                (data[2].toString()),
                (data[2].toString().split("@")[1].split("\\.")[0]),
                        Double.parseDouble(data[3].toString()),
                        data[4].toString(),
                        data[5].toString(),
                        currId);

                person.setImagePath("");
                people.add(person);
                currId += 1;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return people;
    }
    public List<Person> importFromCSV(MultipartFile file, PersonService personService) throws IOException {
        List<Person> importedEmployees = new ArrayList<>();
        List<String> validationErrors = new ArrayList<>();

        // Walidacja pliku
        if (!file.getOriginalFilename().endsWith(".csv")) {
            throw new IllegalArgumentException("Plik nie jest w formacie CSV.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csvReader = new CSVReader(reader)) {

            String[] header = csvReader.readNext(); // Odczytaj nagłówki
            if (!isValidHeader(header)) {
                throw new IllegalArgumentException("Nieprawidłowy format nagłówków w pliku CSV.");
            }

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                try {
                    // Konwersja wiersza CSV na obiekt Person
                    int id = personService.getNewId();
                    Person person = parseCsvLine(line, id);
                    importedEmployees.add(person);
                } catch (IllegalArgumentException e) {
                    validationErrors.add("Błąd w wierszu: " + String.join(",", line) + " - " + e.getMessage());
                }
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException("Błąd walidacji pliku CSV.", e);
        }

        // Opcjonalnie: jeśli wystąpiły błędy, możesz je zapisać w logach lub zwrócić w odpowiedzi.
        if (!validationErrors.isEmpty()) {
            System.err.println("Błędy podczas importu:");
            validationErrors.forEach(System.err::println);
        }

        return importedEmployees;
    }

    private boolean isValidHeader(String[] header) {
        // Walidacja nagłówków
        return header != null && header.length >= 4 &&
                header[0].equalsIgnoreCase("first_name") &&
                header[1].equalsIgnoreCase("last_name") &&
                header[2].equalsIgnoreCase("email") &&
                header[3].equalsIgnoreCase("salary") &&
                header[4].equalsIgnoreCase("currency") &&
                header[5].equalsIgnoreCase("country");
    }

    private Person parseCsvLine(String[] data, int currId) {
        if (data.length < 6) {
            throw new IllegalArgumentException("Niekompletny wiersz.");
        }

        // Parsowanie i walidacja pól
        Person person = new Person(data[0].toString(),
                (data[1].toString()),
                (data[2].toString()),
                (data[2].toString().split("@")[1].split("\\.")[0]),
                Double.parseDouble(data[3].toString()),
                data[4].toString(),
                data[5].toString(),
                currId);
        person.setImagePath("");

        return person;
    }

    private void validateEmail(String email) {
        if (email == null || !email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("Nieprawidłowy email: " + email);
        }
    }
    public String exportToCSV(List<String> columns, List<Person> employees) throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);

        // Add column names as the first row
        csvPrinter.printRecord(columns);
        for (Person employee : employees) {
            List<String> row = new ArrayList<>();
            for (String column : columns) {
                switch (column.toLowerCase()) {
                    case "first_name" -> row.add(employee.getName());
                    case "last_name" -> row.add(employee.getLastName());
                    case "email" -> row.add(employee.getEmail());
                    case "company" -> row.add(employee.getCompanyName());
                    case "salary" -> row.add("" + employee.getSalary());
                    case "currency" -> row.add(employee.getCurrency());
                    case "country" -> row.add(employee.getCountry());
                    case "image_path" -> row.add(employee.getImagePath());
                    default -> throw new IllegalArgumentException("Invalid column: " + column);
                }
            }
            csvPrinter.printRecord(row);
        }
        csvPrinter.flush();
        return stringWriter.toString();
    }

}
