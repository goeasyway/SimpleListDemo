package com.exercise.simplelistdemo.mvp.model;

import java.util.List;

/**
 * Created by lan on 15/7/17.
 */
public class Country {
    /**
     * These members will be used by Retrofit, so you must use the same name with json
     */
    private String title;
    List<Fact> rows;

    public List<Fact> getRows() {
        return rows;
    }

    public String getTitle() {
        return title;
    }
}
