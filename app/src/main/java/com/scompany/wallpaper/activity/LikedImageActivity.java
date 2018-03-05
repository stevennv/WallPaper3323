package com.scompany.wallpaper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scompany.wallpaper.R;
import com.scompany.wallpaper.adapter.LikedAdapter;
import com.scompany.wallpaper.model.ImageFavorite;
import com.scompany.wallpaper.utils.DatabaseLike;

import java.util.ArrayList;
import java.util.List;

public class LikedImageActivity extends AppCompatActivity {
    private RecyclerView rvLiked;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseLike database;
    private List<ImageFavorite> list = new ArrayList<>();
    private LikedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_image);
        iniUI();
    }

    private void iniUI() {
        database = new DatabaseLike(this);
        rvLiked = findViewById(R.id.rv_liked);
        layoutManager = new GridLayoutManager(this, 2);
        rvLiked.setLayoutManager(layoutManager);
        list = database.getAllImage(true);
        adapter = new LikedAdapter(this, list);
        rvLiked.setAdapter(adapter);
    }
}
