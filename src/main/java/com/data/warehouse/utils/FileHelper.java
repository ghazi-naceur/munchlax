package com.data.warehouse.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.data.warehouse.utils.Constants.*;

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
            entity = new HashMap<>();
            for (int i = 0; i < value.length; i++) {
                entity.put(keys[i], value[i]);
            }
            entities.add(entity);
        }
        br.close();
        return entities;
    }
}
