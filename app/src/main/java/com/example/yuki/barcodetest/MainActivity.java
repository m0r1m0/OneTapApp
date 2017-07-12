package com.example.yuki.barcodetest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    List<BookModel> mainListData;
    MainListAdapter adapter;
    int REQUEST_RESULTACTIVITY = 1001;
    private String isbncode;
    private String title;
    private String author;
    private String salesDate;
    private String publisherName;
    private String itemCaption;
    ShowallApi showallApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainListData = new ArrayList<>();

        final ListView mainList = (ListView)findViewById(R.id.mainList);
        adapter = new MainListAdapter(this, mainListData);
        mainList.setAdapter(adapter);

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookModel book = adapter.getItem(position);
                String url = book.getItemurl();
                Log.d("debug",url+"がURLです");
                if (url != "") {
                    Uri uri = Uri.parse(url);
                    Intent i = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "購入ページのURLがありません", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button mBtnCamera = (Button) findViewById(R.id.camera_button);
        mBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug", "onStart");
        // api送信してDBに登録されている情報を取得する処理
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ctl.lineqlog.info/isbn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        showallApi = retrofit.create(ShowallApi.class);

        Call<List<BookModel>> showallBook = showallApi.apiShowallBook("aaa");
        showallBook.enqueue(new Callback<List<BookModel>>() {
            @Override
            public void onResponse(Call<List<BookModel>> call, Response<List<BookModel>> response) {
                mainListData.clear();
                mainListData.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<BookModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "通信に失敗しました", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanResult != null) {
            TextView qResultView = (TextView) findViewById(R.id.qr_text_view);
            Log.d("scan", "==-----:  " + scanResult.getContents());
            // Intentの作成
            Intent intent = new Intent(getApplication(), ResultActivity.class);

            // putExtra(キー, 渡したいデータ)
            String isbn = scanResult.getContents();
            intent.putExtra("scanResult", isbn);
            startActivity(intent);
        }

    }
}
