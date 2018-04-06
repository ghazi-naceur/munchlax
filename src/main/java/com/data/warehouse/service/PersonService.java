package com.data.warehouse.service;


import com.data.warehouse.entity.Person;

import java.util.List;

public interface PersonService {

    Person findById(long id);

    Person findByName(String name);

    void savePerson(Person person);

    void updatePerson(Person person);

    void deletePersonById(long id);

    List<Person> findAllPersons();

    public boolean isPersonExist(Person person);
}
