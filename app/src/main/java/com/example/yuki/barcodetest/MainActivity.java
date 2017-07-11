package com.example.yuki.barcodetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
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

        Call<List<BookModel>> showallBook = showallApi.apiShowallBook(isbncode);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanResult != null) {
            TextView qResultView = (TextView) findViewById(R.id.qr_text_view);
            qResultView.setText(scanResult.getContents());
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
