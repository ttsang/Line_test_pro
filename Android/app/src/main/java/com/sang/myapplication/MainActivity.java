package com.sang.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    ImageFreeTextAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        List<String> listImages = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(AppUtil.JSON_TEST);
            TextView tv_title = findViewById(R.id.title);

            if (jsonObject.has("title")) {
                tv_title.setText(jsonObject.getString("title"));
            }
            if (jsonObject.has("image")) {
                JSONArray array = new JSONArray(jsonObject.getString("image"));
                for (int i = 0; i < array.length(); i++) {
                    String object = array.getString(i);
                    downloadFile(object);
                    listImages.add(object);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RecyclerView recyclerView = findViewById(R.id.rv_images);
        adapter = new ImageFreeTextAdapter(MainActivity.this, listImages);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void downloadFile(final String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new FileAsyncHttpResponseHandler(/* Context */ this) {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, File file) {
                Log.d("Sang", "throwable " + throwable);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, File file) {
                Log.d("Sang", "file" + file);
                adapter.setServerPaths(url);
            }

        });
    }
}
