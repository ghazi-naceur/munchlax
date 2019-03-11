package com.data.warehouse.dao;

import com.data.warehouse.utils.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
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
import java.lang.reflect.InvocationTargetException;
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
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setObject(entity);
            elasticsearchOperations.index(indexQuery);
            return entity;
        } catch (Exception e) {
            LOGGER.error("Error when trying to insert the document '{}' in Elasticsearch : {} ", entity, e.getCause());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T update(T entity) {

        String esId = null;
        String esIndex = null;
        String esType = null;
        try {
            IndexTypeIdExtractor indexTypeIdExtractor = new IndexTypeIdExtractor(entity, esId, esIndex, esType).invoke();
            esId = indexTypeIdExtractor.getEsId();
            esIndex = indexTypeIdExtractor.getEsIndex();
            esType = indexTypeIdExtractor.getEsType();

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
            LOGGER.error("Error when trying to update the document '{}' in Elasticsearch : {} ", entity, e.getCause());
        }
        return entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getById(String id, String index, String type) {
        GetRequest request = new GetRequest(index, type, id);
        try {
            ActionFuture<GetResponse> future = elasticsearchOperations.getClient().get(request);
            GetResponse response = future.get();
            Object object = Serializer.getObject(response.getSource(), request.index());
            return (T)object;
        } catch (Exception e){
            LOGGER.error("Error when trying to get the document with the id '{}' in Elasticsearch : {} ", id, e.getCause());
        }
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

    private class IndexTypeIdExtractor {
        private T entity;
        private String esId;
        private String esIndex;
        private String esType;

        public IndexTypeIdExtractor(T entity, String esId, String esIndex, String esType) {
            this.entity = entity;
            this.esId = esId;
            this.esIndex = esIndex;
            this.esType = esType;
        }

        public String getEsId() {
            return esId;
        }

        public String getEsIndex() {
            return esIndex;
        }

        public String getEsType() {
            return esType;
        }

        public IndexTypeIdExtractor invoke() throws IllegalAccessException, InvocationTargetException {
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
            return this;
        }
    }
}
