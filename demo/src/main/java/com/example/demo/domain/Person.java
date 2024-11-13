package com.example.demo.domain;

public class Person {

    private int id;
    private String name;
    private String lastName;
    private String email;
    private String companyName;
    private double salary;
    private String country;
    private String currency;

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
