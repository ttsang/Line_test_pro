package com.sang.myapplication;

import java.io.*;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import static com.sang.myapplication.AppUtil.storagePath;

public class DownloadImage extends AsyncTask<String, Integer, Bitmap> {
    private static final String TAG = DownloadImage.class.getSimpleName();
    private WeakReference<ImageView> imageView;
    private WeakReference<ProgressBar> progressBar;
    private WeakReference<TextView> tvDownloaded;
    private WeakReference<Context> context;
    private String mUrl;
    private String fileName;

    public DownloadImage(ImageView imageView, ProgressBar progressBar, TextView tv_downloaded, Context context, String url, String name) {
        this.imageView = new WeakReference<>(imageView);
        this.progressBar = new WeakReference<>(progressBar);
        this.tvDownloaded = new WeakReference<>(tv_downloaded);
        this.context = new WeakReference<>(context);
        this.mUrl = url;
        this.fileName = name;
    }

    @Override
    protected void onPreExecute() {
        if (progressBar != null) {
            progressBar.get().setVisibility(View.VISIBLE);
            progressBar.get().setMax(100);
        }
        if (tvDownloaded != null) {
            tvDownloaded.get().setVisibility(View.GONE);
        }
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        File direct = new File(storagePath);
        if (!direct.exists()) {
            direct.mkdirs();
        }
        InputStream inputStream = null;
        Bitmap bitmap = null;
        ByteArrayOutputStream outputStream = null;
        try {
            URL url = new URL(mUrl);
            URLConnection connection = url.openConnection();
            connection.connect();
            //get the length of the file
            int lengthOfFile = connection.getContentLength();
            byte[] buffer = new byte[1024];
            int total = 0;
            int count = 0;
            inputStream = new BufferedInputStream(url.openStream(), 8192);
            outputStream = new ByteArrayOutputStream(lengthOfFile);
            while ((count = inputStream.read(buffer)) != -1) {
                total = total + count;
                publishProgress(new Integer((total * 100) / lengthOfFile));
                outputStream.write(buffer, 0, count);
            }
            bitmap = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, total);

            //create a file to write bitmap data
            File f = new File(storagePath + "/" + this.fileName + ".jpg");
            f.createNewFile();

                //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            OutputStream os = new BufferedOutputStream(fos);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            Log.d(TAG, "file size " + f.length() + " path: " + f.getAbsolutePath());
            PreferenceHelper.getInstance(context.get()).setImageLocal(fileName, f.getAbsolutePath());
            // Save image path to database but I do not implement yet.
            os.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            return null;
        }
        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressBar.get().setProgress(values[0]);
        tvDownloaded.get().setText(String.valueOf(values[0]));
        tvDownloaded.get().setVisibility(View.VISIBLE);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        progressBar.get().setProgress(0);
        progressBar.get().setVisibility(View.GONE);
        tvDownloaded.get().setVisibility(View.VISIBLE);
        tvDownloaded.get().setText("Downloaded");
        if (imageView != null) {
            if (result != null) {
                imageView.get().setImageBitmap(result);
            }
        }
        super.onPostExecute(result);
    }


}
