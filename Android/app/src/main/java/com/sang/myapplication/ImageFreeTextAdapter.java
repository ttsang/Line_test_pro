package com.sang.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageFreeTextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> imgPath;
    private List<String> imgServerPath = new ArrayList<>();

    public ImageFreeTextAdapter(Context mContext, List<String> imgPath) {
        this.imgPath = imgPath;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_freetext, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        if (imgServerPath.size() > 0 && position < imgServerPath.size()) {
            Bitmap bitmap = getProfileImg(imgServerPath.get(position));
            myViewHolder.avatar.setImageBitmap(bitmap);
        }
    }

    public void setServerPaths(String url) {
        if (imgServerPath.size() < imgPath.size()) {
            imgServerPath.add(url);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return imgPath.size();
    }

    public void addPathImage(String path) {
        imgPath.add(path);
        notifyItemInserted(imgPath.size());
    }

    public void addMultiPathImage(List<String> paths) {
        int index = imgPath.isEmpty() ? 0 : imgPath.size();
        imgPath.addAll(index, paths);
        notifyItemRangeInserted(index, paths.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatar;

        ProgressBar pb_loading;

        MyViewHolder(View view) {
            super(view);
            avatar = view.findViewById(R.id.imgFreeText);
            pb_loading = view.findViewById(R.id.progress);
        }

    }


    private Bitmap getProfileImg(String url) {
//        Bitmap bitmap = cache.get(url);
        Bitmap bitmap = null;
        if (bitmap != null) return bitmap;

//        File file = ThumbDAO.downloadProfileImg(getContext(), url);
        File file = new File(url);
        if (file == null) return null;
        FileInputStream inputStream = null;
        //            inputStream = new FileInputStream(file);
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        return bitmap;
    }

}
