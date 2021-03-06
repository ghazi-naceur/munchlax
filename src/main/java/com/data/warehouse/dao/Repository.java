package com.data.warehouse.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by Ghazi Ennacer on 01/01/2019.
 * Email: ghazi.ennacer@gmail.com
 */

public interface Repository<T> {

    T create(T entity);

    void create(String index, String type, Map<String, Object> entity);

    void create(String index, String type, String entity);

    T update(T entity, String index, String type, String id);

    T getById(String id, String index, String type);

    List<T> getAll(String index, String type);

    List<T> findAll(String index, String type, Class<T> clazz);

    void delete(T entity);

    void deleteEntityById(String index, String type, String id);

    Boolean isEntityExist(String index, Map<String, Object> entityAsMap);

    List<T> searchEntities(String index, Map<String, Object> searchEntities);
}
