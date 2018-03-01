package com.scompany.wallpaper.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.scompany.wallpaper.R;
import com.scompany.wallpaper.adapter.ListImageAdapter;
import com.scompany.wallpaper.model.Data;
import com.scompany.wallpaper.model.ImageFavorite;
import com.scompany.wallpaper.utils.BasicImageDownloader;
import com.scompany.wallpaper.utils.Contanst;
import com.scompany.wallpaper.utils.DatabaseLike;

import java.io.File;

public class DetailImageActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private ListImageAdapter adapter;
    private Data data;
    private ImageView imgCopyLink;
    private ImageView imgShare;
    private ImageView imgDownload;
    private ImageView imgLike;
    private String urlImage;
    private String nameImage;
    private ProgressBar progressBar;
    private String imagePath;
    private File myImageFile;
    private DatabaseLike database;
    private ImageFavorite imageFavorite;
    private boolean checkLike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);
        iniUI();
    }

    private void iniUI() {
        database = new DatabaseLike(this);
        viewPager = findViewById(R.id.viewpager);
        imgCopyLink = findViewById(R.id.img_copy_link);
        imgShare = findViewById(R.id.img_share);
        imgDownload = findViewById(R.id.img_download);
        imgLike = findViewById(R.id.img_like);
        progressBar = findViewById(R.id.progressBar);
        if (getIntent() != null) {
            data = (Data) getIntent().getSerializableExtra(Contanst.URL_IMAGE);
            int pos = getIntent().getIntExtra("position", 0);
            adapter = new ListImageAdapter(this, data.getImages(), pos);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(pos);
            urlImage = data.getImages()[viewPager.getCurrentItem()].getImg();
            nameImage = urlImage.replace("/", "");
        }
        checkLikeImage();
        imgCopyLink.setOnClickListener(this);
        imgDownload.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        imgLike.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_copy_link:
                Toast.makeText(this, data.getImages()[viewPager.getCurrentItem()].getSrc(), Toast.LENGTH_SHORT).show();
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("text label", "text to clip");
                clipboard.setPrimaryClip(clip);
                break;
            case R.id.img_download:
                urlImage = data.getImages()[viewPager.getCurrentItem()].getImg();
                downloadImage(urlImage);
                break;
            case R.id.img_share:
                urlImage = data.getImages()[viewPager.getCurrentItem()].getImg();
                try {
                    shareImage();
                } catch (Exception e) {
                    downloadImage(urlImage);
                    shareImage();
                }
                break;
            case R.id.img_like:
                likeImage();
                break;
        }
    }

    private void downloadImage(String url) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait!!!");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
            @Override
            public void onError(BasicImageDownloader.ImageError error) {

            }

            @Override
            public void onProgressChange(int percent) {
                if (percent != 100) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(percent);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onComplete(Bitmap result) {
                final Bitmap.CompressFormat mFormat = Bitmap.CompressFormat.JPEG;
                        /* don't forget to include the extension into the file name */

                myImageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "WallPaper_Android/" + nameImage);
                Log.d("CHECK_FILE_PATH", nameImage);
                BasicImageDownloader.writeToDisk(myImageFile, result, new BasicImageDownloader.OnBitmapSaveListener() {
                    @Override
                    public void onBitmapSaved() {
                        Toast.makeText(DetailImageActivity.this, "Image saved as: " + myImageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onBitmapSaveError(BasicImageDownloader.ImageError error) {
                        Toast.makeText(DetailImageActivity.this, "Error code " + error.getErrorCode() + ": " +
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        dialog.dismiss();
                    }
                }, mFormat, false);

            }
        });
        downloader.download(url, true);
    }

    private void shareImage() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "download this image");
        Uri bitmapUri = Uri.fromFile(myImageFile);
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "Share image via..."));
    }

    private void checkLikeImage() {
        try {
            database.getLikeByName(nameImage);
            imgLike.setImageResource(R.drawable.ic_liked);
            checkLike = true;
        } catch (Exception e) {
            imgLike.setImageResource(R.drawable.ic_like);
            checkLike = false;
        }
    }

    private void likeImage() {
        imageFavorite = new ImageFavorite(nameImage, data.getImages()[viewPager.getCurrentItem()].getSrc());
        if (checkLike) {
            database.deleteImage(imageFavorite);
            imgLike.setImageResource(R.drawable.ic_like);
            Toast.makeText(DetailImageActivity.this, "Dislike", Toast.LENGTH_SHORT).show();
            checkLike = false;
        } else {
            database.addImage(imageFavorite);
            imgLike.setImageResource(R.drawable.ic_liked);
            Toast.makeText(DetailImageActivity.this, "Like", Toast.LENGTH_SHORT).show();
            checkLike = true;
        }
    }
}