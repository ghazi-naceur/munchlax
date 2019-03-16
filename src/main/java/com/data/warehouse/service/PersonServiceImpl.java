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

    public void updatePerson(T person, String index, String type, String id) {
        personDAO.update(person, index, type, id);
    }

    @Override
    public void deletePerson(T entity) {
        personDAO.delete(entity);
    }

    public void deletePersonById(String index, String type, String id) {
        personDAO.deleteEntityById(index, type, id);
    }

    public List<T> findAllPersons(String index, String type) {
        return personDAO.getAll(index, type);
    }

    public boolean isPersonExist(T person) {
        return true;
    }
}
