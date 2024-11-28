package com.example.demo.domain;

import com.example.demo.validation.Currency;
import com.example.demo.validation.TotalSalary;
import com.example.demo.validation.UniqueEmail;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Entity
public class Person {

    @Id
    private int id;

    @NotBlank(message="Name cant be blank")
    @Size(min=3, max=20, message="Name needs to be between 3 and 20 chars long")
    @Pattern(regexp = "^[a-zA-Z\\-]+$", message = "Name can only use letters and dashes")
    private String name;

    @NotBlank(message = "Last name cant be blank")
    @Size(min = 3, max = 30, message = "Last name needs to be between 3 and 30 chars long")
    @Pattern(regexp = "^[a-zA-Z\\-]+$", message = "Last name can only use letters and dashes")
    private String lastName;

    @Email(message = "Invalid emial")
    @NotBlank(message = "Email cant be blank")
    private String email;

    @DecimalMin(value = "0.01", message = "Salary has to be a positive number")
    @DecimalMax(value = "1000000.00", message = "Salary cant be over 1 000 000")
    @TotalSalary(message="Salary cannot go over company budget")
    private double salary;

    @NotBlank(message = "Currency is required")
    @Currency(message = "Wrong currency")
    private String currency;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 30, message = "Country needs to be between 2 and 30 chars")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Country can only use letters")
    private String country;
    private String companyName;
    @Transient
    private MultipartFile photo;
    private String imagePath;

    public Person() {
        System.out.println("Worker: " + this);
    }

    public Person(String name, String lastName, String email, String companyName, double salary, String currency, String country, int id) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.companyName = companyName;
        this.salary = salary;
        this.country = country;
        this.currency = currency;
        System.out.println("Worker: " + this);
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public double getSalary() {
        return salary;
    }

    public String getCountry() {
        return country;
    }

    public String getCurrency() {
        return currency;
    }

    public String getEmail() {
        return email;
    }

    public String getCompanyName() {
        return companyName;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "name= '" + name + '\'' +
                ", lastname = " + lastName +
                ", email = " + email +
                ", company = " + companyName +
                ", salary = " + salary +
                ", currency = " + currency +
                ", country = " + country +
                '}';
    }
}
