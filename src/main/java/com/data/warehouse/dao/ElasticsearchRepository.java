package com.data.warehouse.dao;

import com.data.warehouse.entity.Entity;
import com.data.warehouse.utils.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
            ((Entity) entity).setId(UUID.randomUUID().toString());
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
    public T update(T entity, String index, String type, String id) {

        try {
            UpdateRequest req = new UpdateRequest();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.convertValue(entity, Map.class);
            req.doc(map);
            UpdateQuery request = new UpdateQuery();
            request.setId(id);
            request.setIndexName(index);
            request.setType(type);
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
            return (T) object;
        } catch (Exception e) {
            LOGGER.error("Error when trying to get the document with the id '{}' in Elasticsearch : {} ", id, e.getCause());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAll(String index, String type) {

        List<T> entities = new ArrayList<>();
        try {
            SearchResponse response = elasticsearchOperations.getClient().prepareSearch(index)
                    .setTypes(type)
                    .setQuery(QueryBuilders.matchAllQuery())
                    .execute()
                    .actionGet();
            for (SearchHit hit : response.getHits()) {
                entities.add((T) Serializer.getObject(hit.getSourceAsMap(), index));
            }
            return entities;
        } catch (Exception e) {
            LOGGER.error("Error when trying to retrieve multiple documents from the index '{}' in Elasticsearch : {} ", index, e.getCause());
        }
        return entities;
    }

    @Override
    public void delete(T entity) {
        String esId = null;
        String esIndex = null;
        String esType = null;
        try {
            IndexTypeIdExtractor indexTypeIdExtractor = new IndexTypeIdExtractor(entity, esId, esIndex, esType).invoke();
            esId = indexTypeIdExtractor.getEsId();
            elasticsearchOperations.delete(entity.getClass(), esId);
        } catch (Exception e) {
            LOGGER.error("Error when trying to delete the document with the id '{}' in Elasticsearch : {} ", esId, e.getCause());
        }
    }

    @Override
    public void deleteEntityById(String index, String type, String id) {
        try {
            elasticsearchOperations.delete(index, type, id);
        } catch (Exception e) {
            LOGGER.error("Error when trying to delete the document with the id '{}' in Elasticsearch : {} ", id, e.getCause());
        }
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
