package com.scompany.wallpaper.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.scompany.wallpaper.R;
import com.scompany.wallpaper.adapter.DataAdapter;
import com.scompany.wallpaper.adapter.ListImageAdapter;
import com.scompany.wallpaper.model.Category;
import com.scompany.wallpaper.model.Data;
import com.scompany.wallpaper.model.ImageFavorite;
import com.scompany.wallpaper.model.Images;
import com.scompany.wallpaper.utils.BasicImageDownloader;
import com.scompany.wallpaper.utils.Contanst;
import com.scompany.wallpaper.utils.DatabaseLike;

import java.io.File;

public class WallPaperActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView rvData;
    private RecyclerView.LayoutManager layoutManager;
    private DataAdapter adapter;
    private String[] TITLES = {"New", "Polular", "Advice", "Awesome", "Cartoon", "Cute", "Devotion",
            "Engagement", "Famous", "Flirty", "Flowers"};
    private ImageView imageView;
    private Spinner spnCategory;
    private Images[] list;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_paper);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wall_paper, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(WallPaperActivity.this, LikedImageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void iniUI() {

        gson = new Gson();
        rvData = findViewById(R.id.rv_image);
        layoutManager = new GridLayoutManager(this, 2);
        rvData.setLayoutManager(layoutManager);
        imageView = findViewById(R.id.images);
        spnCategory = findViewById(R.id.spinner);
        getCategory();
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Arrays.fill(list, null);
                getData("http://cdn.skollabs.com/love/v1/" + TITLES[i] + ".js");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                getData("http://cdn.skollabs.com/love/v1/" + TITLES[0] + ".js");
            }
        });
//        Glide.with(this).load("http://78.media.tumblr.com/a925866afba3e8d30f967adec19aa4b4/tumblr_ojvoytrANV1vh1qxzo1_500.jpg").into(imageView);
//        getData("http://cdn.skollabs.com/love/v1/Popular.js");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            boolean permission1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean permission2 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (permission1 && permission2) {
                iniUI();
            }
        }
    }

    private void getData(String url) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait!!!");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        RequestQueue queue = Volley.newRequestQueue(WallPaperActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("123123", response);
                    Data data = gson.fromJson(response, Data.class);
                    list = data.getImages();
                    adapter = new DataAdapter(WallPaperActivity.this, data);
                    adapter.notifyDataSetChanged();
                    rvData.setAdapter(adapter);
                } catch (Exception e) {
                    Log.e("CHECK_ERROR", e.getMessage());
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        });
        queue.add(stringRequest);
    }

    private void getCategory() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait!!!");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        RequestQueue queue = Volley.newRequestQueue(WallPaperActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Contanst.URL_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Category category = gson.fromJson(response, Category.class);
                TITLES = category.getCategories();
                ArrayAdapter adapter = new ArrayAdapter(WallPaperActivity.this, android.R.layout.simple_dropdown_item_1line, TITLES);
                spnCategory.setAdapter(adapter);
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        });
        queue.add(stringRequest);
    }
}
