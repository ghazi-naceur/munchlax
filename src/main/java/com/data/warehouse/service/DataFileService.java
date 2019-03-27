package com.data.warehouse.service;

import com.data.warehouse.entity.DataFiles;

import java.util.List;

/**
 * Created by Ghazi Naceur on 20/03/2019
 * Email: ghazi.ennacer@gmail.com
 */
public interface DataFileService {

    List<String> saveDataFile(DataFiles dataFiles);
}
