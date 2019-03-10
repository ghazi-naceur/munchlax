package com.data.warehouse.controller;


import com.data.warehouse.entity.Person;
import com.data.warehouse.service.PersonService;
import com.data.warehouse.utils.Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("all")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService service;

    // TODO expose a DTO for person .. currently this is not urgent
    @PostMapping
    public void createPerson(@RequestBody Person person) {
        service.savePerson(person);
    }

    @PutMapping
    public void updatePerson(@RequestBody Person person) {
        service.updatePerson(person);
    }

    @GetMapping()
    public List<Person> findAllPersons() {
        return service.findAllPersons();
    }

    @GetMapping(value = "/{id}")
    public Person findPersonById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deletePersonWithId(@PathVariable int id) {
        service.deletePersonById(id);
    }
}
