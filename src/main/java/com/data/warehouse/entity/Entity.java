package com.data.warehouse.entity;

import org.springframework.data.annotation.Id;

/**
 * Created by Ghazi Naceur on 16/03/2019
 * Email: ghazi.ennacer@gmail.com
 */
public class Entity {
    @Id
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
