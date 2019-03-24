# Munchlax (Files Eater)

This project processes XML, CSV and JSON files and insert them into Elasticsearch.

### Prerequisites

 Java 8
 Elasticsearch 6.5.4
 Angular 7

### Getting Started

You provide the path of your files and Munchlax will process them, and it will insert them into Elasticsearch under a specific ES index. All CSV files will be inserted a in a CSV index (The same for JSON and XML files).

### Limitations

For the moment, Munchlax does not support float/double type in the CSV files.
Concerning the XML files, you need to provide files that respect this format :

```
<root>
    <record>
        ... 1st record ...
    </record>

    <record>
        ... 2nd record ...
    </record>

   <record>
        ... 3rd record ...
   </record>
</root>
```

### Enhancements (To be done in the future)

Processing files using the File System feature provided by Apache Camel

###### Note :
This project is still in process ...