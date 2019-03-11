package com.data.warehouse.service;


import com.data.warehouse.entity.Person;

import java.util.List;

public interface PersonService<T> {

    Person findById(String id, String index, String type);

    Person findByName(String name);

    void savePerson(T person);

    void updatePerson(T person);

    void deletePersonById(long id);

    List<Person> findAllPersons();

    public boolean isPersonExist(T person);
}
