package com.data.warehouse.service;

import com.data.warehouse.dao.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Ghazi Naceur on 20/03/2019
 * Email: ghazi.ennacer@gmail.com
 */

@Service
public class DataFileServiceImpl implements DataFileService {

    @Autowired
    private Repository repo;

    @Override
    public void saveDataFile(String index, String type, Map<String, Object> dataFile) {
        repo.create(index, type, dataFile);
    }
}
