package com.sang.myapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private ImageFreeTextAdapter adapter;
    private static final int REQUEST = 112;
    private String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
    private List<ImageModel> listImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // The first hard code get success get movies api
        listImages = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(AppUtil.JSON_TEST);
            TextView tv_title = findViewById(R.id.title);

            if (jsonObject.has("title")) {
                tv_title.setText(jsonObject.getString("title"));
            }
            if (jsonObject.has("image")) {
                JSONArray array = new JSONArray(jsonObject.getString("image"));
                // Need to get local database to get path of image after that compare with api
                // But I do not have time to implement it
                for (int i = 0; i < array.length(); i++) {
                    String object = array.getString(i);
                    if (!TextUtils.isEmpty(object)) {
                        ImageModel imageModel = new ImageModel();
                        imageModel.id = getImageId(object);
                        imageModel.localPath = PreferenceHelper.getInstance(this).getImageLocal(imageModel.id);
                        Log.d("Sang", "flocalPath " + imageModel.localPath);
                        imageModel.serverPath = object;
                        listImages.add(imageModel);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 23) {
//            String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST);
            } else {
                //do here
                initView();
            }
        } else {
            //do here
            initView();
        }
    }

    private void initView() {
//        <!-- I hard code for width and height-->
        RecyclerView recyclerView = findViewById(R.id.rv_images);
        adapter = new ImageFreeTextAdapter(listImages);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void downloadFile(final String url) {
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get(url, new FileAsyncHttpResponseHandler(/* Context */ this) {
//            @Override
//            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, File file) {
//                Log.d("Sang", "throwable " + throwable);
//            }
//
//            @Override
//            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, File file) {
//                Log.d("Sang", "file" + file);
//                adapter.setServerPaths(file.getAbsolutePath());
//            }
//
//        });
    }

    private String getImageId(final String url) {
        for (String id : url.split("/")) {
            if (!TextUtils.isEmpty(id) && id.contains("_JPEG")) {
                return id;
            }
        }
        return "sang_test";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                    initView();
                } else {
                    Toast.makeText(this, "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
