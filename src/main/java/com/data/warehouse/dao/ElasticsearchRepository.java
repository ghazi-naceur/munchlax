package com.data.warehouse.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.update.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
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

    private static Logger LOGGER = LoggerFactory.getLogger(ElasticsearchRepository.class.getName());

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public T create(T entity) {
        try {
            insertIntoElasticsearch(entity);
            return entity;
        } catch (Exception e) {
            LOGGER.error("Error when trying to insert the document '{}' in Elasticsearch : {} ", entity, e.getCause());
        }
        return null;
    }

    @Override
    public T update(T entity) {

        String esId = null;
        String esIndex = null;
        String esType = null;
        try {
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Id.class)) {
                    esId = (String) field.get(entity);
                }
            }

            for (Annotation annotation : entity.getClass().getAnnotations()) {
                Class<? extends Annotation> type = annotation.annotationType();
                for (Method method : type.getDeclaredMethods()) {
                    Object value = method.invoke(annotation, (Object[]) null);
                    if (method.getName().equals("indexName")) {
                        esIndex = (String) value;
                    } else if (method.getName().equals("type")) {
                        esType = (String) value;
                    }
                }
            }

            UpdateRequest req = new UpdateRequest();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.convertValue(entity, Map.class);
            req.doc(map);
            UpdateQuery request = new UpdateQuery();
            request.setId(esId);
            request.setIndexName(esIndex);
            request.setType(esType);
            request.setUpdateRequest(req);
            request.setClazz(entity.getClass());

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
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setObject(entity);
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
}
