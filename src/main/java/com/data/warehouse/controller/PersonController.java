package com.data.warehouse.controller;


import com.data.warehouse.entity.Person;
import com.data.warehouse.service.PersonService;
import com.data.warehouse.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.data.warehouse.utils.Constants.PERSONS_INDEX;
import static com.data.warehouse.utils.Constants.PERSON_TYPE;

@SuppressWarnings("all")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService service;

    // TODO expose a DTO for person + failure cases are not all managed
    @PostMapping
    public ResponseEntity<Void> createPerson(@RequestBody Person person, UriComponentsBuilder ucBuilder) {
        service.savePerson(person);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/{id}").buildAndExpand(person.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> updatePerson(@RequestBody Person person) {
        service.updatePerson(person);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Person>> findAllPersons() {
        List<Person> persons = service.findAllPersons(PERSONS_INDEX, PERSON_TYPE);
        if (persons.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);//You can return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Person> findPersonById(@PathVariable String id) {
        Person person = service.findById(id, PERSONS_INDEX, PERSON_TYPE);
        if (person == null) {
            System.out.println("Person with id " + id + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Person> deletePerson(@RequestBody Person person) {
        service.deletePerson(person);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Person> deletePersonById(@PathVariable String id) {
        service.deletePersonById(PERSONS_INDEX, PERSON_TYPE, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
