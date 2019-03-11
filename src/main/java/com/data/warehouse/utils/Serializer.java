package com.data.warehouse.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

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

    public static Object getObject(Map<String, Object> map, String index){
        return new ObjectMapper().convertValue(map, Matcher.MAPPER.get(index));
    }
}
