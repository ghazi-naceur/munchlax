package com.data.warehouse;

import com.data.warehouse.service.DataFileService;
import com.data.warehouse.service.PersonService;
import com.data.warehouse.utils.Constants;
import com.data.warehouse.utils.ElasticsearchQueryBuilder;
import com.data.warehouse.utils.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.data.warehouse.utils.Constants.CSV_DATA_FILE_INDEX;
import static com.data.warehouse.utils.Constants.CSV_DATA_FILE_TYPE;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(Application.class.getName());

    @Autowired
    PersonService personService;

    @Autowired
    ElasticsearchQueryBuilder request;

    @Autowired
    DataFileService dataFileService;

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .logStartupInfo(false)
                .run(args);
    }

    @Override
    public void run(String... arg0) throws Exception {
        logger.info("Munchlax started ...");
        //TODO missing unit tests
//        personService.savePerson(new Person("Netero", "Isaac", 125, "Hunter"));

        // 1
//        request.getDocumentFromIndex(PERSONS_INDEX, PERSON_TYPE, "10");
        // 2
//        request.getDocumentsFromIndexUsingMatchQuery(PERSONS_INDEX, "lastName", "Takamura");
        // 3
//        request.getDocumentsFromIndexUsingMultiMatchQuery(PERSONS_INDEX, "Takamura", "firstName", "lastName");
        // 4
//        request.getDocumentsFromIndexUsingTermQuery(PERSONS_INDEX, "occupation", "ghi");
        // 5
//        request.getDocumentsFromIndexUsingTermsQuery(PERSONS_INDEX, "occupation",  "ghi", "Shinobi");
        // 6
//        request.getDocumentsFromIndexUsingPrefixQuery(PERSONS_INDEX, "lastName", "Taka");
        // 7
//        request.getDocumentsFromIndexUsingCustomQuery(PERSONS_INDEX, new SearchSourceBuilder().query(
//                QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery("lastName", "Takamura"))
//                                    .must(QueryBuilders.termQuery("occupation", "ghi"))
//        ));
        // 8
//        request.getDocumentsFromIndexUsingQueryStringQuery(PERSONS_INDEX, "lastName", OR, "Takamura", "abc");
//        ReflectionHelper.getNonSpecialFields(new Person("aaa", "bbb", 22, "ccc"));
//        ReflectionHelper.getEsIndex(new Person("aaa", "bbb", 22, "ccc"));
//        ReflectionHelper.getEsType(new Person("aaa", "bbb", 22, "ccc"));
//        List<Map<String, Object>> listOfEntities = FileHelper.processCSVFile("D:\\github-projects\\munchlax\\files\\csv\\data_with_timestamps.csv");
//        listOfEntities.forEach(map -> {
//            dataFileService.saveDataFile(CSV_DATA_FILE_INDEX, CSV_DATA_FILE_TYPE, map);
//        });

        // Process and insert multiple CSV files
        Collection<File> files = FileHelper.listFilesInFolder(new File("D:\\github-projects\\munchlax\\files\\csv"));
        files.forEach(file -> {
            try {
                FileHelper.processCSVFile(file.getAbsolutePath()).forEach(map -> {
                    dataFileService.saveDataFile(CSV_DATA_FILE_INDEX, CSV_DATA_FILE_TYPE, map);
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}