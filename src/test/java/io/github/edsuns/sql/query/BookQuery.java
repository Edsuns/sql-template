package io.github.edsuns.sql.query;

import io.github.edsuns.sql.protocol.Query;

import java.util.List;

/**
 * @author edsuns@qq.com
 * @since 2024/3/20 17:50
 */
public class BookQuery implements Query {

    private Long id;
    private String name;
    private String nameLike;
    private List<String> names;
    private Long price;
    private Long limit;

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

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }
}
