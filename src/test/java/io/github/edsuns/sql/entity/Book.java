package io.github.edsuns.sql.entity;

import io.github.edsuns.sql.protocol.Entity;

/**
 * @author edsuns@qq.com
 * @since 2023/03/31 15:51
 */
public class Book implements Entity {

    private Long id;

    private String name;

    private Long price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
