package com.example.yuki.barcodetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultActivity extends AppCompatActivity {

    private String isbncode;
    private String title;
    private String author;
    private String salesDate;
    private String publisherName;
    private String itemCaption;
    IsbnApi service;
    RegisterApi register;

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
                title = response.body().get(0).getTitle();
                author = response.body().get(0).getAuthor();
                salesDate = response.body().get(0).getSalesDate();
                publisherName = response.body().get(0).getPublisherName();
                itemCaption = response.body().get(0).getItemCaption();

                TextView titleText = (TextView) findViewById(R.id.title_text);
                TextView authorText = (TextView) findViewById(R.id.author_text);
                TextView salesDateText = (TextView) findViewById(R.id.salesDate_text);
                TextView publisherText = (TextView) findViewById(R.id.publisher_text);
                TextView itemCaptionText = (TextView) findViewById(R.id.itemCaption_text);
                titleText.setText(title);
                authorText.setText(author);
                salesDateText.setText(salesDate);
                publisherText.setText(publisherName);
                itemCaptionText.setText(itemCaption);
            }

            @Override
            public void onFailure(Call<List<BookModel>> call, Throwable t) {
                Log.d("isbn", "失敗しました。(エラー："+t+" )");
            }
        });
    }

    public void onClickRegister(View view) {
        Log.d("debug", "OnClickRegister");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ctl.lineqlog.info/isbn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        register = retrofit.create(RegisterApi.class);
        Call<List<BookModel>> registerBookCall = register.apiRegisterBook(isbncode);
        registerBookCall.enqueue(new Callback<List<BookModel>>() {
            @Override
            public void onResponse(Call<List<BookModel>> call, Response<List<BookModel>> response) {
                Log.d("isbn", "onResponse: " + response.body().get(0).getResult());
                String reslut = response.body().get(0).getResult();
                Log.d("debug",reslut);
                Toast.makeText(getApplicationContext(), "登録しました", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<List<BookModel>> call, Throwable t) {
                Log.d("isbn", "失敗しました。(エラー："+t+" )");
            }
        });

    }

    public void onClickBack(View view) {
        finish();
    }
}
