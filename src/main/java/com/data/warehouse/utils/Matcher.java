package com.data.warehouse.utils;

import com.data.warehouse.entity.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.data.warehouse.utils.Constants.PERSONS_INDEX;

/**
 * Created by Ghazi Naceur on 28/02/2019
 * Email: ghazi.ennacer@gmail.com
 * <p>
 * Matches between the index and its respective class
 */
public final class Matcher {

    private Matcher() {
        super();
    }

    public static final Map<String, Class> MAPPER;
    public static final Map<Class, String> INVERTED_MAPPER;
    static {
        MAPPER = new HashMap<>();
        MAPPER.put(PERSONS_INDEX, Person.class);
        INVERTED_MAPPER = getInvertedMapper();
    }

    private static Map<Class, String> getInvertedMapper() {
        return MAPPER.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
