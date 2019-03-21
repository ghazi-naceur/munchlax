package com.data.warehouse.service;

import com.data.warehouse.dao.Repository;
import com.data.warehouse.entity.DataFiles;
import com.data.warehouse.utils.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import static com.data.warehouse.utils.Constants.*;

/**
 * Created by Ghazi Naceur on 20/03/2019
 * Email: ghazi.ennacer@gmail.com
 */

@Service
public class DataFileServiceImpl implements DataFileService {

    private static Logger logger = LoggerFactory.getLogger(DataFileServiceImpl.class.getName());

    @Autowired
    private Repository repo;

    @Override
    public void saveDataFile(DataFiles dataFiles) {

        Collection<File> files = FileHelper.listFilesInFolder(new File(dataFiles.getPath()));
        for (File file : files) {
            try {
                if (file.getName().endsWith(".csv")) {
                    FileHelper.processCSVFile(file.getAbsolutePath()).forEach(map -> {
                        repo.create(CSV_DATA_FILE_INDEX, CSV_DATA_FILE_TYPE, map);
                    });
                } else if (file.getName().endsWith(".json")) {
                    for (Map<String, Object> map : FileHelper.processJSONFile(file.getAbsolutePath())) {
                        repo.create(JSON_DATA_FILE_INDEX, JSON_DATA_FILE_TYPE, map);
                    }
                }
            } catch (Exception e) {
                logger.error("Error occurred when trying to save the data file from the path {} caused by : {}", dataFiles.getPath(), e);
            }
        }
    }
}
