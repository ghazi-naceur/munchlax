package com.data.warehouse.utils;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.data.warehouse.utils.Constants.FROM;
import static com.data.warehouse.utils.Constants.RESULT_SIZE;

/**
 * Created by Ghazi Naceur on 26/02/2019
 * Email: ghazi.ennacer@gmail.com
 */

@SuppressWarnings("unchecked")
@Service
public class ElasticsearchQueryBuilder<T> {

    // TODO Need to implement logger log4j + exclude the spring logger implementation

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

    public List<T> getDocumentsFromIndexUsingMatchQuery(String index, String field, String value) throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchQuery(field, value))
                .from(FROM).size(RESULT_SIZE);
        return formatResult(index, builder);
    }

    // TODO implementing a custom exception instead of RuntimeException
    private List<T> formatResult(String index, SearchSourceBuilder builder) throws IOException {
        return getDocument(index, builder).stream().map(document -> {
            try {
                return (T) Serializer.unmarshallSourceFromString(document.getSourceAsString(), document.getIndex());
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }).collect(Collectors.toList());
    }

    private List<SearchHit> getDocument(String index, SearchSourceBuilder builder) throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        return Arrays.asList(response.getHits().getHits());
    }

    public List<T> getDocumentsFromIndexUsingMultiMatchQuery(String index, String value, String... fieldNames) throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.multiMatchQuery(value, fieldNames))
                .from(FROM).size(RESULT_SIZE);
        return formatResult(index, builder);
    }

    public List<T> getDocumentsFromIndexUsingTermQuery(String index, String field, String value) throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.termQuery(field, value))
                .from(FROM).size(RESULT_SIZE);
        return formatResult(index, builder);
    }

    public List<T> getDocumentsFromIndexUsingTermsQuery(String index, String field, String... values) throws IOException {
        // values field must be in lowercase format
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.termsQuery(field,
                Arrays.stream(values).map(String::toLowerCase).collect(Collectors.toList())))
                .from(FROM).size(RESULT_SIZE);
        return formatResult(index, builder);
    }

    public List<T> getDocumentsFromIndexUsingPrefixQuery(String index, String field, String prefixValue) throws IOException {
        // prefix value => lowercase
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.prefixQuery(field, prefixValue.toLowerCase()))
                .from(FROM).size(RESULT_SIZE);
        return formatResult(index, builder);
    }

    public List<T> getDocumentsFromIndexUsingCustomQuery(String index, SearchSourceBuilder builder) throws IOException {
        return formatResult(index, builder);
    }

}
