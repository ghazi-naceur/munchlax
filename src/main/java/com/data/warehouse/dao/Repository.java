package com.data.warehouse.dao;

import java.util.List;

/**
 * Created by Ghazi Ennacer on 01/01/2019.
 * Email: ghazi.ennacer@gmail.com
 */

public interface Repository<T> {

    T create(T entity);

    T update(T entity);

    T getById(String id, String index, String type);

    List<T> getAll(String table, Class<T> clazz);

    void delete(T entity);
}
