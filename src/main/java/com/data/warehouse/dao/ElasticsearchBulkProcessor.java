package com.data.warehouse.dao;

import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor.Listener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ElasticsearchBulkProcessor implements Listener {

    private static Logger logger = LoggerFactory.getLogger(ElasticsearchBulkProcessor.class);

    ElasticsearchBulkProcessor() {
        super();
    }

    @Override
    public void beforeBulk(long executionId, BulkRequest request) {
    }

    @Override
    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
        BulkItemResponse[] bulkItemResponses = response.getItems();

        if (bulkItemResponses.length > 0) {
            logger.debug("actions completed : {}", bulkItemResponses.length);

            for (BulkItemResponse tempResponse : bulkItemResponses) {
                StringBuilder temp = new StringBuilder();
                if (tempResponse.isFailed()) {
                    temp.append("Failed :: (")
                            .append(tempResponse.getFailureMessage())
                            .append(")")
                            .append(tempResponse.getIndex())
                            .append(" :: ")
                            .append(tempResponse.getType())
                            .append(" :: ")
                            .append(tempResponse.getId());
                    logger.error("Bulk result: {}", temp);
                } else {
                    String index = tempResponse.getIndex();
                    temp.append("Success :: ")
                            .append(index).append(" :: ")
                            .append(tempResponse.getType())
                            .append(" :: ")
                            .append(tempResponse.getId());
                    logger.info("Bulk result: {}", temp);
                }
            }
        }
    }

    @Override
    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
        logger.error("Bulk execution failed.", failure);
        List<DocWriteRequest> bulkItemResponses = request.requests();
        if (bulkItemResponses != null) {
            for (DocWriteRequest tempRequest : bulkItemResponses) {
                if (tempRequest instanceof IndexRequest) {
                    logger.error("Failed single request {}", tempRequest.id());
                }
            }
        }
        System.exit(0);
    }
}
