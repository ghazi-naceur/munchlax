package com.data.warehouse.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.update.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by Ghazi Ennacer on 01/01/2019.
 * Email: ghazi.ennacer@gmail.com
 */
@org.springframework.stereotype.Repository
public class ElasticsearchRepository<T> implements Repository<T> {

//    static Logger LOGGER = Logger.getLogger(ElasticsearchRepository.class.getName());

    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    @Override
    public T create(T entity) {
        try {
            insertIntoElasticsearch(entity);
            return entity;
        } catch (Exception e) {
//            LOGGER.error("Error when trying to insert the document '" + entity + "' in Elasticsearch : " + e + " - " + e.getCause());
        }
        return null;
    }

    @Override
    public T update(T entity) {

//        String esId = getElasticId(entity);
        UpdateRequest req = new UpdateRequest();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(entity, Map.class);
        req.doc(map);
        UpdateQuery request = new UpdateQuery();
//        request.setId(esId);
        request.setUpdateRequest(req);
        request.setClazz(entity.getClass());
        try {
            elasticsearchOperations.update(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public T getById(String idName, String idValue, String table, Class<T> clazz) {
        return null;
    }

    @Override
    public List<T> getAll(String table, Class<T> clazz) {
        return null;
    }

    @Override
    public void delete(T entity) {
//        String esId = getElasticId(entity);
//        elasticsearchOperations.delete(entity.getClass(), esId);
    }

    public void insertIntoElasticsearch(T entity) {
        try {
//            String esId = getElasticId(entity);

            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setObject(entity);

            // All this work is guaranteed by @Document(indexName="persons", type="person")

//            indexQuery.setSource(entity.toString());
//            indexQuery.setId("1");
//            Annotation[] annotations = entity.getClass().getAnnotations();
//            for (Annotation annotation : annotations) {
//                if(annotation.annotationType() == Document.class) {
//                    for (Method method : annotation.annotationType().getDeclaredMethods()) {
//                        Object value = method.invoke(annotation, (Object[]) null);
//                        if (method.getName() != null && method.getName().equals("indexName")){
//                            indexQuery.setIndexName((String)value);
//                        }
//                        if (method.getName() != null && method.getName().equals("type")){
//                            indexQuery.setType((String)value);
//                        }
//                    }
//                }
//            }
            elasticsearchOperations.index(indexQuery);
        } catch (Exception e) {
            e.printStackTrace();
//            LOGGER.error("Error when trying to insert the '" + entity + "' : " + e + " - " + e.getCause());
        }
    }

    public void insertIntoElasticsearchWithFixedId(T entity) {
        try {
            String esId = "fixed_id";

            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setObject(entity);
            indexQuery.setId(esId);
            indexQuery.setIndexName("persons");
            indexQuery.setType("person");

            elasticsearchOperations.index(indexQuery);
        } catch (Exception e) {
//            LOGGER.error("Error when trying to insert the '" + entity + "' : " + e + " - " + e.getCause());
        }
    }

    /**
     public String getElasticId(T entity) {
     String esId = "";
     try {
     Field[] fields = entity.getClass().getDeclaredFields();
     for (Field field : fields) {
     field.setAccessible(true);
     if (field.isAnnotationPresent(PrimaryKeyColumn.class) || field.isAnnotationPresent(PrimaryKey.class)) {
     esId += field.get(entity);
     }
     }
     esId = esId.replaceAll("\\s+", "");
     } catch (Exception e) {
     //            LOGGER.error("Error when trying to retrieve the id '" + esId + "' from the entity '" + entity + "' :: " + e + " - " + e.getCause());
     }
     return esId;
     }
     */

}
