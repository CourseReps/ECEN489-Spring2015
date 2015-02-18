package com.pokemonmasterkc.sqlitemon;

/**
 * Created by kwilk_000 on 2/15/2015.
 */

public class Values {

    private long id;
    private String value;

    public Values() {
        super();
    }

    public Values(String value) {
        super();
        this.value = value;
    }

    public Values(long id, String value) {
        super();
        this.id = id;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}