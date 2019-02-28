package com.data.warehouse.utils;

import com.data.warehouse.entity.Person;

import java.util.HashMap;
import java.util.Map;

import static com.data.warehouse.utils.Constants.PERSONS_INDEX;

/**
 * Created by Ghazi Naceur on 28/02/2019
 * Email: ghazi.ennacer@gmail.com
 *
 * Matches between the indice and its respective class
 */
public class Matcher {

    public Matcher() {
        super();
    }

    static final Map<String, Class> MAPPER;
    static {
        MAPPER = new HashMap<>();
        MAPPER.put(PERSONS_INDEX, Person.class);
    }
}
