package com.sang.myapplication;

import android.os.Build;
import android.os.Environment;

import java.io.File;

public class AppUtil {
    public static final String JSON_TEST = "{\n" +
            "    \"title\" : \"Civil War\",\n" +
            "    \"image\" : [\n" +
            "        \"http://movie.phinf.naver.net/20151127_272/1448585271749MCMVs_JPEG/movie_image.jpg?type=m665_443_2\",\n" +
            "        \"http://movie.phinf.naver.net/20151127_84/1448585272016tiBsF_JPEG/movie_image.jpg?type=m665_443_2\",\n" +
            "        \"http://movie.phinf.naver.net/20151125_36/1448434523214fPmj0_JPEG/movie_image.jpg?type=m665_443_2\"\n" +
            ",\"https://images.unsplash.com/photo-1570125909961-96fb5f0238ad?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80\"\n" +
            "    ]\n" +
            "}";

    private static final String downloadManagerLocalPath = Environment.DIRECTORY_DOWNLOADS + "/LineTest";
    public static final String storagePath = Environment.getExternalStorageDirectory()
            + "/" + downloadManagerLocalPath;

    public static File getImage(String imagename) {

        File mediaImage = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);
            if (!myDir.exists())
                return null;
            mediaImage = new File(storagePath + "/" + imagename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaImage;
    }

    public static boolean checkifImageExists(String imagename) {
        File file = getImage(imagename);
        return file != null && file.exists();
    }
}
