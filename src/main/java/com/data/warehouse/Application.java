package com.data.warehouse;

import com.data.warehouse.dao.PersonRepository;
import com.data.warehouse.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    PersonRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... arg0) throws Exception {

        repository.deleteAll();

        repository.save(new Person(1,"Netero", "Isaac", 125, "Hunter"));
        repository.save(new Person(2, "Uchiha", "Itachi", 27, "Shinobi"));
        repository.save(new Person(3, "Mamoru", "Takamura", 29, "Boxer"));
    }
}