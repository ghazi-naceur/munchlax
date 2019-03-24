package com.data.warehouse.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.data.warehouse.utils.Constants.COMMA_SEPARATOR;

/**
 * Created by Ghazi Naceur on 20/03/2019
 * Email: ghazi.ennacer@gmail.com
 */

public final class FileHelper {

    private FileHelper() {
        super();
    }

    public static List<Map<String, Object>> processCSVFile(String path) throws IOException {

        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        String[] keys;
        List<String[]> values = new ArrayList<>();
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        keys = br.readLine().split(COMMA_SEPARATOR);
        List<String> valueLines = br.lines().collect(Collectors.toList());

        for (String line : valueLines) {
            values.add(line.split(COMMA_SEPARATOR));
        }

        for (String[] value : values) {
            //  if value contains a float value (with a , ), we will obtain an
            //          and ArrayOutOffBoundException because value.length > keys.length
            //          or we can loop on keys : for (int i = 0; i < keys.length; i++)
            //          but the problem, we will loose the column with no header
            entity = new HashMap<>();
            for (int i = 0; i < value.length; i++) {
                entity.put(keys[i], value[i]);
            }
            entities.add(entity);
        }
        br.close();
        return entities;
    }

    public static List<Map<String, Object>> processJSONFile(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference valueTypeRef = new TypeReference<List<Map<String, Object>>>() {
        };
        return objectMapper.readValue(new File(path), valueTypeRef);
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> processXMLFile(String path) throws TransformerException, IOException, SAXException, ParserConfigurationException {

        Document xmlDocument = convertXMLFileToXMLDocument(path);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));
        String xmlString = writer.getBuffer().toString();
        JSONObject xmlJSONObj = XML.toJSONObject(xmlString);
        return (List<Map<String, Object>>) toMap((JSONObject) xmlJSONObj.get("root")).get("record");
    }

    public static Collection<File> listFilesInFolder(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            return Arrays.asList(files);
        }
        return Collections.emptySet();
    }

    private static Document convertXMLFileToXMLDocument(String filePath) throws ParserConfigurationException, IOException, SAXException {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        //Create DocumentBuilder with default configuration
        builder = factory.newDocumentBuilder();
        //Parse the content to Document object
        return builder.parse(new File(filePath));
    }

    private static Map<String, Object> toMap(JSONObject object) throws JSONException {

        Map<String, Object> map = new HashMap<>();
        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    private static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
