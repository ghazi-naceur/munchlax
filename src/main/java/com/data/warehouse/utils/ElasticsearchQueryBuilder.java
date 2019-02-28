package com.data.warehouse.utils;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Ghazi Naceur on 26/02/2019
 * Email: ghazi.ennacer@gmail.com
 */

@Service
public class ElasticsearchQueryBuilder<T> {

    // TODO Need to implement logger + exclude the spring logger implementation

    RestHighLevelClient client;

    public ElasticsearchQueryBuilder(RestHighLevelClient client) {
        this.client = client;
    }

    public T getDocumentFromIndex(String index, String type, String docId) throws IOException {
        T document = null;
        GetRequest getRequest = new GetRequest(index, type, docId);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

        if (getResponse.isExists()) {
            String source = getResponse.getSourceAsString();
            document = (T) Serializer.unmarshallSourceFromString(source, index);
        }
        return document;
    }

}
