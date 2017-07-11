package com.example.yuki.barcodetest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by YUKI on 2017/07/05.
 */

public interface RegisterApi {
    @GET("api")
    Call<List<BookModel>> apiRegisterBook(
            @Query("register") String isbn
    );
}
