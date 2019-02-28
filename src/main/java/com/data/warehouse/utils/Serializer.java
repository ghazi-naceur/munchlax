package com.data.warehouse.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Ghazi Naceur on 28/02/2019
 * Email: ghazi.ennacer@gmail.com
 */
@SuppressWarnings("unchecked")
public final class Serializer {

    private Serializer() {
        super();
    }

    static Object unmarshallSourceFromString(String source, String index) throws IOException {
        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(source, Matcher.MAPPER.get(index));
    }
}
