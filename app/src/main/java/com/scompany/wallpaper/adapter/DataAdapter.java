package com.scompany.wallpaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.scompany.wallpaper.utils.Contanst;
import com.scompany.wallpaper.R;
import com.scompany.wallpaper.activity.DetailImageActivity;
import com.scompany.wallpaper.model.Data;
import com.scompany.wallpaper.model.Images;

/**
 * Created by Admin on 2/28/2018.
 */

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Data list;

    public DataAdapter(Context context, Data list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_image, parent, false);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        final Images images = list.getImages()[position];
        Glide.with(context).load(images.getImg()).into(myViewHolder.imgData);
        myViewHolder.imgData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailImageActivity.class);
                intent.putExtra(Contanst.URL_IMAGE, list);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.getImages().length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgData;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgData = itemView.findViewById(R.id.img_images);
        }
    }
}
