package com.scompany.wallpaper.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;
import com.scompany.wallpaper.R;
import com.scompany.wallpaper.customview.TouchImageView;
import com.scompany.wallpaper.model.ImageFavorite;
import com.scompany.wallpaper.model.Images;

import java.util.List;

/**
 * Created by Admin on 2/28/2018.
 */

public class ListImageAdapter extends PagerAdapter {
    private Context context;
    private Images[] list;
    private List<ImageFavorite> strings;
    private int pos;

    public ListImageAdapter(Context context, Images[] list, List<ImageFavorite> strings, int pos) {
        this.context = context;
        this.list = list;
        this.pos = pos;
        this.strings = strings;
    }

    @Override
    public int getCount() {
        int size;
        if (list != null) {
            size = list.length;
        } else {
            size = strings.size();
        }
        return size;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView;
        LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemView = mLayoutInflater.inflate(R.layout.item_image_detail, container, false);
        ImageView imgDetail = itemView.findViewById(R.id.img_detail);
        try {
            Glide.with(context).load(list[position].getImg()).into(imgDetail);
        } catch (Exception e) {
            Glide.with(context).load(strings.get(position).getSrc()).into(imgDetail);
        }
        container.addView(itemView);
        return itemView;
    }

}
