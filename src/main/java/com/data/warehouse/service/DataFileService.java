package com.data.warehouse.service;

import java.util.Map;

/**
 * Created by Ghazi Naceur on 20/03/2019
 * Email: ghazi.ennacer@gmail.com
 */
public interface DataFileService {

    void saveDataFile(String index, String type, Map<String, Object> dataFile);
}
