package com.data.warehouse.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.data.warehouse.utils.Constants.COMMA_SEPARATOR;

/**
 * Created by Ghazi Naceur on 20/03/2019
 * Email: ghazi.ennacer@gmail.com
 */

public final class FileHelper {

    private FileHelper() {
        super();
    }

    public static List<Map<String, Object>> processCSVFile(String path) throws IOException {

        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        String[] keys;
        List<String[]> values = new ArrayList<>();
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        keys = br.readLine().split(COMMA_SEPARATOR);
        List<String> valueLines = br.lines().collect(Collectors.toList());

        for (String line : valueLines) {
            values.add(line.split(COMMA_SEPARATOR));
        }

        for (String[] value : values) {
            //  if value contains a float value (with a , ), we will obtain an
            //          and ArrayOutOffBoundException because value.length > keys.length
            //          or we can loop on keys : for (int i = 0; i < keys.length; i++)
            //          but the problem, we will loose the column with no header
            entity = new HashMap<>();
            for (int i = 0; i < value.length; i++) {
                entity.put(keys[i], value[i]);
            }
            entities.add(entity);
        }
        br.close();
        return entities;
    }

    public static List<Map<String, Object>> processJSONFile(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference valueTypeRef = new TypeReference<List<Map<String, Object>>>() {
        };
        return objectMapper.readValue(new File(path), valueTypeRef);
    }

    public static Collection<File> listFilesInFolder(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            return Arrays.asList(files);
        }
        return Collections.emptySet();
    }
}
