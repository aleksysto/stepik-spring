package com.example.demo.domain;

public class Person {

    private String name;
    private String lastName;
    private String email;
    private String companyName;

    public Person() {
        System.out.println("Worker: " + this);
    }

    public Person(String name, String lastName, String email, String companyName) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.companyName = companyName;
        System.out.println("Worker: " + this);
    }


    public void setName(String name) {
        this.name = name;
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

    public String getName() {
        return name;
    }
    public String getLastName() {
        return lastName;
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
                '}';
    }
}
