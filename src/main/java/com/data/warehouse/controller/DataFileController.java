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
    private DataFileService service;

    @PostMapping
    public ResponseEntity<Void> savaDataFile(@RequestBody DataFiles dataFiles, UriComponentsBuilder ucBuilder) {
        service.saveDataFile(dataFiles);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
