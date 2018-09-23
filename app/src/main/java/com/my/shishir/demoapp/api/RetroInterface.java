package com.my.shishir.demoapp.api;

import com.my.shishir.demoapp.model.ProductData;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;

import rx.Observable;

interface RetroInterface {

    @GET("deliveries")
    Observable<List<ProductData>> getData(@Query("limit") int limit, @Query("offset") int offset);
}
