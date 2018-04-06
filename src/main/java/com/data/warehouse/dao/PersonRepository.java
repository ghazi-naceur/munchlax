package com.data.warehouse.dao;

import com.data.warehouse.entity.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PersonRepository extends MongoRepository<Person, Integer> {
}
