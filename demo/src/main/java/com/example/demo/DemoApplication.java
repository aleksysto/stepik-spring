package com.example.demo;

import com.example.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import com.example.demo.domain.Person;

import java.util.List;

@SpringBootApplication
@ImportResource("classpath:beans.xml")
public class DemoApplication {


	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DemoApplication.class, args);
		PersonService personService = context.getBean(PersonService.class);
		personService.displayKeyEmployees();

		System.out.println("\nAll Employees:");
		List<Person> allEmployees = personService.getAllWorkers();
		allEmployees.forEach(System.out::println);

		System.out.println("\nEmployees from Google:");
		List<Person> googleEmployees = personService.filterByCompany("Google");
		googleEmployees.forEach(System.out::println);

		System.out.println("\nEmployees sorted by last name:");
		List<Person> sortedByLastName = personService.sortByLastName();
		sortedByLastName.forEach(System.out::println);
	}

}

