package com.data.warehouse.dao;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ghazi Naceur on 24/03/2019
 * Email: ghazi.ennacer@gmail.com
 */
@Repository
public class BulkPerformer {

    private static Logger logger = LoggerFactory.getLogger(BulkPerformer.class.getName());

    @Autowired
    private ElasticsearchOperations elasticAgent;

    private BulkProcessor bulkRequest = null;

    private void prepareBulk() {

        if (bulkRequest == null) {
            bulkRequest = BulkProcessor.builder(elasticAgent.getClient(), new ElasticsearchBulkProcessor())
                    .setBulkActions(10000)
                    .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                    .setFlushInterval(TimeValue.timeValueSeconds(3))
                    .setBackoffPolicy(
                            BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(1000), 3))
                    .setConcurrentRequests(1)
                    .build();
        }
    }

    public void addToBulk(String index, String type, Map<String, Object> entity) {
        prepareBulk();
        try {
            IndexRequest request = new IndexRequest(index, type, UUID.randomUUID().toString());
            request.source(entity);
            bulkRequest.add(request);
        } catch (Exception e) {
            logger.error("An error occurred when trying to insert the {} in the index {}, caused by : {}", entity, index, e);
        }
    }

    public void closeBulk() {
        if (bulkRequest != null) {
            try {
                bulkRequest.awaitClose(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                logger.error("An error occurred when trying to close the bulk, caused by : {}", e);
            }
            bulkRequest.close();
        }
    }
}
