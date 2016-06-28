package com.exercise.simplelistdemo.mvp.model;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by lan on 15/7/17.
 */
public interface FactRestService {
    final String SERVICE_ENDPOINT = "https://dl.dropboxusercontent.com/u/746330/facts.json";

    /**
     * Use Retrofit to get JSON from URL, then parse it.
     * @return Observable
     */
    @GET("/")
    Observable<Country> getCountry();


}
