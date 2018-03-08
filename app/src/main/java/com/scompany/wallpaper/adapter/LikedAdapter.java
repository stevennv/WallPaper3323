package com.scompany.wallpaper.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.scompany.wallpaper.R;
import com.scompany.wallpaper.activity.DownLoadActivity;
import com.scompany.wallpaper.model.ImageFavorite;
import com.scompany.wallpaper.utils.Contanst;
import com.scompany.wallpaper.utils.DatabaseLike;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin pc on 3/1/2018.
 */

public class LikedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ImageFavorite> list;
    private DatabaseLike databaseLike;
    private boolean isLiked;

    public LikedAdapter(Context context, List<ImageFavorite> list, boolean isLiked) {
        this.context = context;
        this.list = list;
        databaseLike = new DatabaseLike(context);
        this.isLiked = isLiked;
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
        final ImageFavorite imageFavorite = list.get(position);
        Glide.with(context).load(imageFavorite.getSrc()).into(myViewHolder.imgData);
        if (isLiked) {
            myViewHolder.imgLike.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.imgLike.setVisibility(View.GONE);
        }
        myViewHolder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setMessage("Do you want remove this image in list favorite")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                list.remove(position);
                                notifyDataSetChanged();
                                databaseLike.deleteImage(imageFavorite);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });

        myViewHolder.imgData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DownLoadActivity.class);
                intent.putExtra(Contanst.URL_IMAGE, (Serializable) list);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgData;
        public ImageView imgLike;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgData = itemView.findViewById(R.id.img_images);
            imgLike = itemView.findViewById(R.id.img_like);
        }
    }
}
