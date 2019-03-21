package com.data.warehouse.controller;

import com.data.warehouse.entity.DataFiles;
import com.data.warehouse.service.DataFileService;
import com.data.warehouse.utils.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.util.Collection;

import static com.data.warehouse.utils.Constants.CSV_DATA_FILE_INDEX;
import static com.data.warehouse.utils.Constants.CSV_DATA_FILE_TYPE;

/**
 * Created by Ghazi Naceur on 21/03/2019
 * Email: ghazi.ennacer@gmail.com
 */

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/datafile")
public class DataFileController {

    private static Logger logger = LoggerFactory.getLogger(DataFileService.class.getName());

    @Autowired
    DataFileService service;

    @PostMapping
    public ResponseEntity<Void> savaDataFile(@RequestBody DataFiles dataFiles, UriComponentsBuilder ucBuilder) {

        Collection<File> files = FileHelper.listFilesInFolder(new File(dataFiles.getPath()));
        files.forEach(file -> {
            try {
                FileHelper.processCSVFile(file.getAbsolutePath()).forEach(map -> {
                    service.saveDataFile(CSV_DATA_FILE_INDEX, CSV_DATA_FILE_TYPE, map);
                });
            } catch (Exception e) {
                logger.error("Error occurred when trying to save the data file from the path {} caused by : {}", dataFiles.getPath(), e);
            }
        });

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
