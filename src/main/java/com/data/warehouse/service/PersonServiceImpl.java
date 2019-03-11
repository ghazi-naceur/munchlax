package com.data.warehouse.service;


import com.data.warehouse.dao.Repository;
import com.data.warehouse.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl<T> implements PersonService<T> {

    @Autowired
    Repository<T> personDAO;

    public Person findById(String id, String index, String type) {
        return (Person) personDAO.getById(id, index, type);
    }

    public Person findByName(String name) {
        return null;
    }

    public void savePerson(T person) {
        personDAO.create(person);
    }

    public void updatePerson(T person) {
        personDAO.update(person);
    }

    @Override
    public void deletePersonById(long id) {

    }

    public void deletePersonById(Integer id) {
//        personDAO.delete(id);
    }

    public List<Person> findAllPersons() {
//        return personDAO.getAll();
        return null;
    }

    public boolean isPersonExist(T person) {
        return true;
    }
}
