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
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;

import java.io.IOException;
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
            logger.error("Error when trying to insert the document '{}' in Elasticsearch : {} ", entity, e.getCause());
        }
        return null;
    }

    @Override
    public void create(String index, String type, Map<String, Object> entity) {
        try {
            builder.indexEntity(index, type, UUID.randomUUID().toString(), entity);
        } catch (IOException e) {
            logger.error("Error when trying to insert the document '{}' in Elasticsearch : {} ", entity, e.getCause());
        }
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
            logger.error("Error when trying to update the document '{}' in Elasticsearch : {} ", entity, e.getCause());
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
            logger.error("Error when trying to get the document with the id '{}' in Elasticsearch : {} ", id, e.getCause());
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
            logger.error("Error when trying to retrieve multiple documents from the index '{}' in Elasticsearch : {} ", index, e.getCause());
        }
        return entities;
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

    @SuppressWarnings("unchecked")
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
}
