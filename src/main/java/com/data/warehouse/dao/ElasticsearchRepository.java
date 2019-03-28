package com.data.warehouse.dao;

import com.data.warehouse.entity.Entity;
import com.data.warehouse.utils.ElasticsearchQueryBuilder;
import com.data.warehouse.utils.ReflectionHelper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;

import java.io.IOException;
import java.util.*;

/**
 * Created by Ghazi Ennacer on 01/01/2019.
 * Email: ghazi.ennacer@gmail.com
 */
@SuppressWarnings("unchecked")
@org.springframework.stereotype.Repository
public class ElasticsearchRepository<T> implements Repository<T> {

    public static final String ERROR_MESSAGE = "Error when trying to insert the document '{}' in Elasticsearch : {} ";
    private static Logger logger = LoggerFactory.getLogger(ElasticsearchRepository.class.getName());

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ElasticsearchQueryBuilder builder;

    @Override
    public T create(T entity) {
        try {
            ((Entity) entity).setId(UUID.randomUUID().toString());
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setObject(entity);
            elasticsearchOperations.index(indexQuery);
            return entity;
        } catch (Exception e) {
            logger.error(ERROR_MESSAGE, entity, e.getCause());
        }
        return null;
    }

    @Override
    public void create(String index, String type, Map<String, Object> entity) {
        try {
            builder.indexEntity(index, type, UUID.randomUUID().toString(), entity);
        } catch (IOException e) {
            logger.error(ERROR_MESSAGE, entity, e.getCause());
        }
    }

    @Override
    public void create(String index, String type, String entity) {
        try {
            builder.indexEntityAsString(index, type, UUID.randomUUID().toString(), entity);
        } catch (IOException e) {
            logger.error(ERROR_MESSAGE, entity, e.getCause());
        }
    }

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
            logger.error("Error when trying to update the document '{}' in Elasticsearch : {} ", entity, e.getCause());
        }
        return entity;
    }

    @Override
    public T getById(String id, String index, String type) {
        GetRequest request = new GetRequest(index, type, id);
        try {
            ActionFuture<GetResponse> future = elasticsearchOperations.getClient().get(request);
            GetResponse response = future.get();
            Object object = Serializer.getObject(response.getSource(), request.index());
            return (T) object;
        } catch (Exception e) {
            logger.error("Error when trying to get the document with the id '{}' in Elasticsearch : {} ", id, e.getCause());
        }
        return null;
    }

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
            logger.error("Error when trying to retrieve multiple documents from the index '{}' in Elasticsearch : {} ", index, e.getCause());
        }
        return entities;
    }

    public List<T> findAll(String index, String type, Class<T> clazz) {
        try {
            SearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withIndices(index)
                    .withTypes(type)
                    .withQuery(QueryBuilders.matchAllQuery())
                    .build();

            List<T> result = new ArrayList<>();
            Page<T> entities = elasticsearchOperations.startScroll(1000, searchQuery, clazz);
            if (entities != null && !entities.getContent().isEmpty()) {
                String scrollId = ((ScrolledPage<T>) entities).getScrollId();
                boolean stillHasDocuments = true;
                while (stillHasDocuments) {
                    result.addAll(entities.getContent());
                    entities = scroll(scrollId, clazz);
                    if (entities == null || entities.getContent().isEmpty()) {
                        stillHasDocuments = false;
                    }
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("Error when trying to retrieve all documents, caused by : {}", e);
        }
        return Collections.emptyList();
    }

    private Page<T> scroll(String scrollId, Class<T> clazz) {
        try {
            return elasticsearchOperations.continueScroll(scrollId, 1000, clazz);
        } catch (Exception e) {
            logger.error("Error when trying to scroll with the scroll_id {} in order to retrieve documents related to the entity {}, caused by  : {}", scrollId, clazz.getSimpleName(), e);
        }
        return null;
    }

    @Override
    public void delete(T entity) {
        String esId = null;
        try {
            esId = ReflectionHelper.getEsId((Entity) entity);
            elasticsearchOperations.delete(entity.getClass(), esId);
        } catch (Exception e) {
            logger.error("Error when trying to delete the document with the id '{}' in Elasticsearch : {} ", esId, e.getCause());
        }
    }

    @Override
    public void deleteEntityById(String index, String type, String id) {
        try {
            elasticsearchOperations.delete(index, type, id);
        } catch (Exception e) {
            logger.error("Error when trying to delete the document with the id '{}' in Elasticsearch : {} ", id, e.getCause());
        }
    }

    @Override
    public Boolean isEntityExist(String index, Map<String, Object> entityAsMap) {

        try {
            List<T> entities = builder.getDocumentsUsingEntityAsMap(index, entityAsMap);
            if (!entities.isEmpty()) {
                logger.error("The entity from the index {} with the id {} already exist ", index, ((Entity) entities.get(0)).getId());
                return true;
            }
        } catch (IOException e) {
            logger.error("An error occurred when trying to search for entity from the index {} : {}", index, e);
        }
        return false;
    }

    @Override
    public List<T> searchEntities(String index, Map<String, Object> searchEntities) {

        try {
            return builder.getDocumentsUsingEntityAsMap(index, searchEntities);
        } catch (IOException e) {
            logger.error("An error occurred when trying to search for entity from the index {} : {}", index, e);
        }
        return Collections.emptyList();
    }
}
