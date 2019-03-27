package com.data.warehouse.controller;

import com.data.warehouse.entity.DataFiles;
import com.data.warehouse.service.DataFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ghazi Naceur on 21/03/2019
 * Email: ghazi.ennacer@gmail.com
 */
@SuppressWarnings("all")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/datafile")
public class DataFileController {

    private static Logger logger = LoggerFactory.getLogger(DataFileController.class.getName());

    @Autowired
    private DataFileService service;

    @PostMapping
    public ResponseEntity<List<String>> savaDataFile(@RequestBody DataFiles dataFiles, UriComponentsBuilder ucBuilder) {
        HttpHeaders headers = new HttpHeaders();
        List<String> files = new ArrayList<>();
        try {
            files = service.saveDataFile(dataFiles);
            logger.info("All files in the provided path {} are processed and inserted into Elasticsearch", dataFiles.getPath());
            return new ResponseEntity<List<String>>(files, headers, HttpStatus.CREATED);
        } catch (Exception e){
            logger.error("An error occurred when trying to process the files located under the path {}, caused by {}", dataFiles.getPath(), e);
            return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
