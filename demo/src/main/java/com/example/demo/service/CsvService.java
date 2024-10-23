package com.example.demo.service;

import com.example.demo.domain.Person;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    public List<Person> readCsvFile(String fileName) {
        List<Person> people = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileName)))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");

                Person person = new Person(data[0].toString(),
                (data[1].toString()),
                (data[2].toString()),
                (data[2].toString().split("@")[1].split("\\.")[0]));

                people.add(person);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return people;
    }
}