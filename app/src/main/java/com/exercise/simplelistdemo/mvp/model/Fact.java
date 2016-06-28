package com.exercise.simplelistdemo.mvp.model;

/**
 * Created by lan on 15/7/17.
 */
public class Fact {
    /**
     * These members will be used by Retrofit, so you must use the same name with json
     */
    private String title;
    private String description;
    private String imageHref;

    public String getDescription() {
        return description;
    }

    public String getImageHref() {
        return imageHref;
    }

    public String getTitle() {
        return title;
    }
}
