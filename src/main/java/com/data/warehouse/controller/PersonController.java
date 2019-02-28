package com.data.warehouse.controller;


import com.data.warehouse.entity.Person;
import com.data.warehouse.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Slf4j
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    PersonService service;

    @RequestMapping(method = RequestMethod.POST)
    public void createPerson(@RequestBody Person person) {
        service.savePerson(person);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updatePerson(@RequestBody Person person) {
        service.updatePerson(person);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Person> findAllPersons() {
        return service.findAllPersons();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Person findPersonById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deletePersonWithId(@PathVariable int id) {
        service.deletePersonById(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteAllPersons() {
//        service.deletePersonById();
    }

}
