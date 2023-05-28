package com.vodafone.model;

import lombok.Builder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Builder
public class Author {
    @Id
    @GeneratedValue
    @NotNull
    private Integer id;
    @NotNull
    private String name;

    public Author(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Author() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
