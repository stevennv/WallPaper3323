package com.scompany.wallpaper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.scompany.wallpaper.R;
import com.scompany.wallpaper.adapter.LikedAdapter;
import com.scompany.wallpaper.model.ImageFavorite;
import com.scompany.wallpaper.utils.Contanst;
import com.scompany.wallpaper.utils.DatabaseLike;

import java.util.ArrayList;
import java.util.List;

public class LikedImageActivity extends AppCompatActivity {
    private RecyclerView rvLiked;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseLike database;
    private List<ImageFavorite> list = new ArrayList<>();
    private LikedAdapter adapter;
    private Toolbar toolbar;
    private TextView tvTitleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_image);
        iniUI();
    }

    private void iniUI() {
        database = new DatabaseLike(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvTitleToolbar = findViewById(R.id.tv_title_toolbar_2);
        rvLiked = findViewById(R.id.rv_liked);
        layoutManager = new GridLayoutManager(this, 2);
        rvLiked.setLayoutManager(layoutManager);
        if (getIntent() != null) {
            String type = getIntent().getStringExtra(Contanst.TYPE);
            tvTitleToolbar.setText(type);
            if (type.equals("Downloaded")) {
                list = database.getAllImage(false);
                adapter = new LikedAdapter(this, list, false);
            } else {
                list = database.getAllImage(true);
                adapter = new LikedAdapter(this, list, true);
            }
        }
        rvLiked.setAdapter(adapter);
    }
}
