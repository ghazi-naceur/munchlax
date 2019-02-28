package com.data.warehouse.dao;

import java.util.List;

/**
 * Created by Ghazi Ennacer on 01/01/2019.
 * Email: ghazi.ennacer@gmail.com
 */

public interface Repository<T> {

    T create(T entity);

    T update(T entity);

    T getById(String idName, String idValue, String table, Class<T> clazz);

    List<T> getAll(String table, Class<T> clazz);

    void delete(T entity);
}
