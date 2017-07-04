package com.example.yuki.barcodetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultActivity extends AppCompatActivity {

    private String isbncode;
    IsbnApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        isbncode = intent.getStringExtra("scanResult");
        Log.d("isbn","isbncode:"+isbncode);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ctl.lineqlog.info/isbn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IsbnApi.class);

        Call<List<BookModel>> bookListCall = service.apiGetBookList(isbncode);

        bookListCall.enqueue(new Callback<List<BookModel>>() {
            @Override
            public void onResponse(Call<List<BookModel>> call, Response<List<BookModel>> response) {
                Log.d("isbn", "onResponse: " + response.body().get(0).getAuthor());
            }

            @Override
            public void onFailure(Call<List<BookModel>> call, Throwable t) {
                Log.d("isbn", "失敗しました。"+t);
            }
        });

    }
}
