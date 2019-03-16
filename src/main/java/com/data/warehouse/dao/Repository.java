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

    List<T> getAll(String index, String type);

    void delete(T entity);

    void deleteEntityById(String index, String type, String id);
}
