package com.data.warehouse.service;


import com.data.warehouse.dao.PersonRepository;
import com.data.warehouse.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("personService")
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository personDAO;

    public Person findById(long id) {
        Integer i = (int) (long) id;
        return personDAO.findOne(i);
    }

    public Person findByName(String name) {
        return null;
    }

    public void savePerson(Person person) {
        personDAO.save(person);
    }

    public void updatePerson(Person person) {
        personDAO.insert(person);
    }

    @Override
    public void deletePersonById(long id) {

    }

    public void deletePersonById(Integer id) {
        personDAO.delete(id);
    }

    public List<Person> findAllPersons() {
        return personDAO.findAll();
    }

    public boolean isPersonExist(Person person) {
        return true;
    }
}
