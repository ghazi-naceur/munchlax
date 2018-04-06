package com.data.warehouse;

import com.data.warehouse.dao.PersonRepository;
import com.data.warehouse.entity.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

    @Autowired
    PersonRepository repository;

    @Test
    public void createPerson() {
        Person person = new Person(4, "Freecss", "Don", 300, "Hunter");
    }

    @Test
    public void findAllPersons() {
        List<Person> persons = repository.findAll();
        assertNotNull(persons);
        assertTrue(!persons.isEmpty());
    }

    @Test
    public void findPersonById() {
        Person person = repository.findOne(1);
        assertNotNull(person);
    }

    @Test
    public void deleteBookWithId() {
        Person person = repository.findOne(1);
        repository.delete(person);
        assertNotNull(person);
    }
}