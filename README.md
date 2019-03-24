# Munchlax (Files Eater)

This project processes XML, CSV and JSON files and insert them into Elasticsearch.

### Prerequisites

 Java 8 <br/>
 Elasticsearch 6.5.4 <br/>
 Angular 7 <br/>

### Getting Started

You provide the path of your files and Munchlax will process them, and it will insert them into Elasticsearch under a specific ES index. <br/>
You can provide your path through the graphical user interface or through a REST webservice :
```
POST http://localhost:8080/datafile
{
  "path": "path\\files\\csv"
}
```
All CSV files will be inserted a in a CSV index (The same for JSON and XML files).<br/>
To consult your data, you can search in your indices :
```
CSV : http://localhost:9200/csvs/_search?pretty
JSON : http://localhost:9200/jsons/_search?pretty
XML : http://localhost:9200/xmls/_search?pretty
```

### Limitations

For the moment, Munchlax does not support columns with ',' in the CSV files, like float/double type. So if you have a float type (ex : 123,456), you should replace ',' with '.' (ex : 123.456). The comma ',' is interpreted as a separator between columns, so if there is an additional ',' in the column itself, the number of columns values will be greater than the number of the CSV file columns header.<br/>

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
### Enhancements (To be implemented)

Processing files using the File System feature provided by Apache Camel.<br/>
Inserting datafiles with an ES Bulk.

###### Note :
This project is still in process ...