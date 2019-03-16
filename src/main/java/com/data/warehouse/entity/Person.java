package com.data.warehouse.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Document(indexName = "persons", type = "person")
public class Person extends Entity implements Comparable<Person>, Serializable {

    private String firstName;

    private String lastName;

    private Integer age;

    private String occupation;

    @JsonCreator
    public Person(@JsonProperty(value = "firstName") String firstName,
                  @JsonProperty(value = "lastName") String lastName,
                  @JsonProperty(value = "age") Integer age,
                  @JsonProperty(value = "occupation") String occupation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.occupation = occupation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    @Override
    public int compareTo(Person person) {
        return id.compareTo(person.getId()) +
                firstName.compareTo(person.getFirstName()) +
                lastName.compareTo(person.getLastName()) +
                age.compareTo(person.getAge()) +
                occupation.compareTo(person.getOccupation());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> person = new HashMap<>();
        person.put("id", this.id);
        person.put("firstName", this.firstName);
        person.put("lastName", this.lastName);
        person.put("age", this.age);
        person.put("occupation", this.occupation);
        return person;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", occupation='" + occupation + '\'' +
                '}';
    }
}
