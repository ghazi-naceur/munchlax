package com.data.warehouse.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Document(collection = "PERSONS")
public class Person {


    @Id
    @Field("id")
    private int id;

    @NotNull
    @Size(min = 1)
    @Field("firstName")
    private String firstName;

    @NotNull
    @Size(min = 1)
    @Field("lastName")
    private String lastName;

    @NotNull
    @Size(min = 1)
    @Field("age")
    private Integer age;

    @NotNull
    @Size(min = 1)
    @Field("occupation")
    private String occupation;

    public Person(int id, String firstName, String lastName, Integer age, String occupation) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.occupation = occupation;
    }
}
