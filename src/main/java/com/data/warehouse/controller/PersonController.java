package com.data.warehouse.controller;


import com.data.warehouse.entity.Person;
import com.data.warehouse.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

import static com.data.warehouse.utils.Constants.PERSONS_INDEX;
import static com.data.warehouse.utils.Constants.PERSON_TYPE;

@SuppressWarnings("all")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/persons")
public class PersonController {

    private static Logger logger = LoggerFactory.getLogger(PersonController.class.getName());

    @Autowired
    private PersonService service;

    @PostMapping
    public ResponseEntity<Void> createPerson(@RequestBody Person person, UriComponentsBuilder ucBuilder) {

        HttpHeaders headers = new HttpHeaders();
        try {
            if (service.isPersonExist(PERSONS_INDEX, person.toMap())) {
                logger.warn("This person is already created {}", person.toString());
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            service.savePerson(person);
            headers.setLocation(ucBuilder.path("/{id}").buildAndExpand(person.getId()).toUri());
            logger.info("This person is created successfully {}", person.toString());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("An error occurred when trying to create a person {}, caused by {}", person.toString(), e);
            return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<Void> updatePerson(@RequestBody Person person) {
        HttpHeaders headers = new HttpHeaders();
        try {
            service.updatePerson(person, PERSONS_INDEX, PERSON_TYPE, person.getId());
            logger.info("This person is updated successfully {}", person.toString());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("An error occurred when trying to update a person {}, caused by {}", person.toString(), e);
            return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public ResponseEntity<List<Person>> findAllPersons() {
        HttpHeaders headers = new HttpHeaders();
        try {
            List<Person> persons = service.findAllPersons(PERSONS_INDEX, PERSON_TYPE);
            if (persons.isEmpty()) {
                logger.warn("Your persons index is empty.");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);//You can return HttpStatus.NOT_FOUND
            }
            return new ResponseEntity<>(persons, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("An error occurred when trying to retrieve the first page of persons, caused by {}", e);
            return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Person> findPersonById(@PathVariable String id) {
        HttpHeaders headers = new HttpHeaders();
        try {
            Person person = service.findById(id, PERSONS_INDEX, PERSON_TYPE);
            if (person == null) {
                logger.warn("Person with id {} is not found.", person.getId());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            logger.info("Person with id {} is retrieved.", person.getId());
            return new ResponseEntity<>(person, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("An error occurred when trying to retrieve a person with the id {}, caused by {}", id, e);
            return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<Person> deletePerson(@RequestBody Person person) {
        HttpHeaders headers = new HttpHeaders();
        try {
            service.deletePerson(person);
            logger.info("Person with id {} is deleted successfully.", person.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("An error occurred when trying to delete a person with the id {}, caused by {}", person.getId(), e);
            return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Person> deletePersonById(@PathVariable String id) {
        HttpHeaders headers = new HttpHeaders();
        try {
            service.deletePersonById(PERSONS_INDEX, PERSON_TYPE, id);
            logger.info("Person with id {} is deleted successfully.", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("An error occurred when trying to delete a person with the id {}, caused by {}", id, e);
            return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/search")
    public ResponseEntity<List<Person>> searchPersons(@RequestBody Map<String, Object> searchCriteria, UriComponentsBuilder ucBuilder) {

        HttpHeaders headers = new HttpHeaders();
        try {
            List<Person> person = service.searchPersons(PERSONS_INDEX, searchCriteria);
            logger.info("A person search request is performed successfully.");
            return new ResponseEntity<List<Person>>(person, headers, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("An error occurred when trying to search for a person, caused by {}", e);
            return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
