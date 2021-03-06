package com.data.warehouse.service;


import com.data.warehouse.dao.Repository;
import com.data.warehouse.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PersonServiceImpl<T> implements PersonService<T> {

    @Autowired
    private Repository<T> personDAO;

    public Person findById(String id, String index, String type) {
        return (Person) personDAO.getById(id, index, type);
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
//        return personDAO.getAll(index, type);
        return personDAO.findAll(index, type, (Class<T>) Person.class);
    }

    public boolean isPersonExist(String index, Map<String, Object> entityAsMap) {
        return personDAO.isEntityExist(index, entityAsMap);
    }

    @Override
    public List<Person> searchPersons(String index, Map<String, Object> searchCriteria) {
        return (List<Person>) personDAO.searchEntities(index, searchCriteria);
    }
}
