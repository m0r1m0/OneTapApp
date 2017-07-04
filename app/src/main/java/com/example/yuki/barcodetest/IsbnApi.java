package com.example.yuki.barcodetest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface IsbnApi {
    @GET("api")
    Call<List<BookModel>> apiGetBookList(
            @Query("isbn") String isbn
    );
}
