package com.data.warehouse;

import com.data.warehouse.dao.Repository;
import com.data.warehouse.entity.Person;
import com.data.warehouse.service.PersonService;
import com.data.warehouse.utils.Constants;
import com.data.warehouse.utils.ElasticsearchQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.data.warehouse.utils.Constants.PERSON;
import static com.data.warehouse.utils.Constants.PERSONS_INDEX;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    PersonService personService;

    @Autowired
    ElasticsearchQueryBuilder builder;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... arg0) throws Exception {

//        personService.savePerson(new Person("1", "Netero", "Isaac", 125, "Hunter"));
//        personService.savePerson(new Person("2", "Uchiha", "Itachi", 27, "Shinobi"));
//        personService.savePerson(new Person("3", "Mamoru", "Takamura", 29, "Boxer"));
        builder.getDocumentFromIndex(PERSONS_INDEX, PERSON, "10");
    }
}