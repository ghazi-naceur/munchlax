package com.data.warehouse.service;

import com.data.warehouse.dao.BulkPerformer;
import com.data.warehouse.dao.Repository;
import com.data.warehouse.entity.DataFiles;
import com.data.warehouse.utils.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    @Autowired
    private BulkPerformer performer;

    @Override
    public List<String> saveDataFile(DataFiles dataFiles) {
        List<String> processedFiles = new ArrayList<>();
        try {
            Collection<File> files = FileHelper.listFilesInFolder(new File(dataFiles.getPath()));
            for (File file : files) {
                processedFiles.add(file.getAbsolutePath());
                processAndAddToBulk(dataFiles, file);
            }
            return processedFiles;
        } catch (Exception e) {
            logger.error("Error occurred when trying to save the data file from the path {} caused by : {}", dataFiles.getPath(), e);
        } finally {
            // The external try-catch-finally used to close the BulkProcessor : performer.closeBulk().
            // If I put it in the internal try-catch, it will be closed straight after processing the first file
//            performer.closeBulk();
        }
        return Collections.emptyList();
    }

    private void processAndAddToBulk(DataFiles dataFiles, File file) {
        try {
            if (file.getName().endsWith(CSV_EXTENSION)) {
                FileHelper.processCSVFile(file.getAbsolutePath()).forEach(map -> performer.addToBulk(CSV_DATA_FILE_INDEX, CSV_DATA_FILE_TYPE, map));
            } else if (file.getName().endsWith(JSON_EXTENSION)) {
                FileHelper.processJSONFile(file.getAbsolutePath()).forEach(map -> performer.addToBulk(JSON_DATA_FILE_INDEX, JSON_DATA_FILE_TYPE, map));
            } else if (file.getName().endsWith(XML_EXTENSION)) {
                FileHelper.processXMLFile(file.getAbsolutePath()).forEach(map -> performer.addToBulk(XML_DATA_FILE_INDEX, XML_DATA_FILE_TYPE, map));
            }
        } catch (Exception e) {
            logger.error("Error occurred when trying to save the data file from the path {} caused by : {}", dataFiles.getPath(), e);
        }
    }
}
