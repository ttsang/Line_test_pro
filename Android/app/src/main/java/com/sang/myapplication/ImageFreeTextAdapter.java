package com.sang.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.sang.myapplication.AppUtil.checkifImageExists;
import static com.sang.myapplication.AppUtil.storagePath;

public class ImageFreeTextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ImageModel> imageModels;
    private List<String> imgServerPath = new ArrayList<>();


    public ImageFreeTextAdapter(List<ImageModel> movies) {
        this.imageModels = movies;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_freetext, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        final ImageModel imageModel = getItem(position);
        if (imageModel != null) {
            if (TextUtils.isEmpty(imageModel.localPath)) {
                DownloadImage downloadImage = new DownloadImage(myViewHolder.avatar,
                        myViewHolder.pb_loading,
                        myViewHolder.tv_downloaded,
                        myViewHolder.avatar.getContext(), imageModel.serverPath, imageModel.id);
                downloadImage.execute();
            } else {
                Bitmap bitmap = getProfileImg(storagePath + "/" + imageModel.id + ".jpg");
                if (bitmap != null) {
                    myViewHolder.avatar.setImageBitmap(bitmap);
                    myViewHolder.pb_loading.setVisibility(View.GONE);
                    myViewHolder.tv_downloaded.setVisibility(View.VISIBLE);
                    myViewHolder.tv_downloaded.setText("Local file");
                } else {
                    DownloadImage downloadImage = new DownloadImage(myViewHolder.avatar,
                            myViewHolder.pb_loading,
                            myViewHolder.tv_downloaded,
                            myViewHolder.avatar.getContext(), imageModel.serverPath, imageModel.id);
                    downloadImage.execute();
                }
            }

            //  For test click download
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadImage downloadImage = new DownloadImage(myViewHolder.avatar,
                            myViewHolder.pb_loading,
                            myViewHolder.tv_downloaded,
                            myViewHolder.avatar.getContext(), imageModel.serverPath, imageModel.id);
                    downloadImage.execute();
                }
            });
        }
    }


    private ImageModel getItem(int pos) {
        if (pos > -1 && pos < imageModels.size())
            return imageModels.get(pos);
        return null;
    }

    @Override
    public int getItemCount() {
        return imageModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView tv_downloaded;
        ProgressBar pb_loading;

        MyViewHolder(View view) {
            super(view);
            avatar = view.findViewById(R.id.imgFreeText);
            pb_loading = view.findViewById(R.id.progress);
            tv_downloaded = view.findViewById(R.id.tv_downloaded);
        }

    }


    private Bitmap getProfileImg(String url) {
//        Bitmap bitmap = cache.get(url);
        Log.e("Sang", "url:" + url);
        Bitmap bitmap = null;
//        if (bitmap != null) return bitmap;

//        File file = ThumbDAO.downloadProfileImg(getContext(), url);
        File file = new File(url);
        if (!file.exists())
            return null;
        //            inputStream = new FileInputStream(file);
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        Log.e("Sang", "bitmap:" + bitmap);
        return bitmap;
    }

}
