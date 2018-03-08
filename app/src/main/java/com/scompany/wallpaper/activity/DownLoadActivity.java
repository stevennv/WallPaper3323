package com.scompany.wallpaper.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.scompany.wallpaper.R;
import com.scompany.wallpaper.adapter.ListImageAdapter;
import com.scompany.wallpaper.model.ImageFavorite;
import com.scompany.wallpaper.model.Images;
import com.scompany.wallpaper.utils.BasicImageDownloader;
import com.scompany.wallpaper.utils.CommonUtil;
import com.scompany.wallpaper.utils.Contanst;
import com.scompany.wallpaper.utils.DatabaseLike;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class DownLoadActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private ListImageAdapter adapter;
    private ImageView imgLike, imgShare, imgDownload, imgSetWP;
    private Images[] images;
    private ImageView imgClose;
    private boolean checkLike;
    private DatabaseLike database;
    private ProgressBar progressBar;
    private String nameImage;
    private File myImageFile;
    private ImageFavorite imageFavorite;
    private Bitmap bitmap;
    private List<ImageFavorite> imageFavorites;
    private String urlImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
//        iniUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        iniUI();
    }


    private void iniUI() {
        database = new DatabaseLike(this);
        viewPager = findViewById(R.id.viewpager);
        imgDownload = findViewById(R.id.img_download);
        imgSetWP = findViewById(R.id.img_copy_link);
        imgShare = findViewById(R.id.img_share);
        imgLike = findViewById(R.id.img_like);
        imgClose = findViewById(R.id.img_close);
        progressBar = findViewById(R.id.progressBar);
        if (getIntent() != null) {
            imageFavorites = database.getAllImage(true);
            int pos = getIntent().getIntExtra("position", 0);
            images = null;
            adapter = new ListImageAdapter(this, images, imageFavorites, pos);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(pos);
            urlImage = imageFavorites.get(viewPager.getCurrentItem()).getSrc();
        }
        checkLikeImage(urlImage);
        imgClose.setOnClickListener(this);
        imgLike.setOnClickListener(this);
        imgDownload.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        imgSetWP.setOnClickListener(this);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                myImageFile = null;
                checkLikeImage(imageFavorites.get(viewPager.getCurrentItem()).getSrc());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_copy_link:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("Do you want set this image to wall paper?")
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                WallpaperManager m = WallpaperManager.getInstance(DownLoadActivity.this);
                                if (myImageFile != null) {
                                    bitmap = BitmapFactory.decodeFile(myImageFile.getPath());
                                    try {
                                        m.setBitmap(bitmap);
                                        Toast.makeText(DownLoadActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Log.d("onClick213", "onClick: " + e.getMessage());
                                    }
                                    dialogInterface.dismiss();
                                } else {
                                    Toast.makeText(DownLoadActivity.this, "You must download this image", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
//                Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
//                startActivity(Intent.createChooser(intent, "Select Wallpaper"));
                break;
            case R.id.img_download:
                urlImage = imageFavorites.get(viewPager.getCurrentItem()).getSrc();
                downloadImage(urlImage, false);
                break;
            case R.id.img_like:
                likeImage();
                break;
            case R.id.img_share:
                urlImage = imageFavorites.get(viewPager.getCurrentItem()).getSrc();
                try {
                    shareImage();
                } catch (Exception e) {
                    downloadImage(urlImage, true);
                }
                break;
            case R.id.img_close:
                finish();
                break;
        }
    }

    private void downloadImage(final String url, final boolean isShare) {
        try {
            database.getLikeBySrc(url, false);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage("This image downloaded!!! \n You want redownload it?")
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (CommonUtil.isOnline(DownLoadActivity.this)) {
                                final ProgressDialog dialog = new ProgressDialog(DownLoadActivity.this);
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
                                        nameImage = String.valueOf(new Date().getTime());
                                        myImageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                                                File.separator + "WallPaper_Android/" + nameImage + "." + mFormat.name().toLowerCase());
                                        Log.d("CHECK_FILE_PATH", nameImage);
                                        bitmap = BitmapFactory.decodeFile(myImageFile.getPath());
                                        BasicImageDownloader.writeToDisk(myImageFile, result, new BasicImageDownloader.OnBitmapSaveListener() {
                                            @Override
                                            public void onBitmapSaved() {
                                                Toast.makeText(DownLoadActivity.this, "Image saved as: " + myImageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                                if (isShare) {
                                                    shareImage();
                                                }
                                            }

                                            @Override
                                            public void onBitmapSaveError(BasicImageDownloader.ImageError error) {
                                                Toast.makeText(DownLoadActivity.this, "Error code " + error.getErrorCode() + ": " +
                                                        error.getMessage(), Toast.LENGTH_LONG).show();
                                                error.printStackTrace();
                                                dialog.dismiss();
                                            }
                                        }, mFormat, false);

                                    }
                                });
                                downloader.download(url, true);
                            } else {
                                Toast.makeText(DownLoadActivity.this, "Please! Check your connection!!!", Toast.LENGTH_SHORT).show();
                            }
                            dialogInterface.dismiss();
                        }
                    }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        } catch (Exception e) {
            if (CommonUtil.isOnline(this)) {
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
                        nameImage = String.valueOf(new Date().getTime());
                        myImageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                                File.separator + "WallPaper_Android/" + nameImage + "." + mFormat.name().toLowerCase());
                        Log.d("CHECK_FILE_PATH", nameImage);
                        bitmap = BitmapFactory.decodeFile(myImageFile.getPath());
                        BasicImageDownloader.writeToDisk(myImageFile, result, new BasicImageDownloader.OnBitmapSaveListener() {
                            @Override
                            public void onBitmapSaved() {
                                Toast.makeText(DownLoadActivity.this, "Image saved as: " + myImageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                imageFavorite = new ImageFavorite(nameImage, imageFavorites.get(viewPager.getCurrentItem()).getSrc(), false);
                                database.addImage(imageFavorite, false);
                                if (isShare) {
                                    shareImage();
                                }
                            }

                            @Override
                            public void onBitmapSaveError(BasicImageDownloader.ImageError error) {
                                Toast.makeText(DownLoadActivity.this, "Error code " + error.getErrorCode() + ": " +
                                        error.getMessage(), Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                                dialog.dismiss();
                            }
                        }, mFormat, false);

                    }
                });
                downloader.download(url, true);
            } else {
                Toast.makeText(this, "Please! Check your connection!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void shareImage() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "download this image");
        Uri bitmapUri = Uri.fromFile(myImageFile);
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "Share image via..."));
    }

    private void checkLikeImage(String nameSearch) {
        try {
            database.getLikeBySrc(nameSearch, true);
            imgLike.setImageResource(R.drawable.ic_liked);
            checkLike = true;
        } catch (Exception e) {
            imgLike.setImageResource(R.drawable.ic_like);
            checkLike = false;
        }
    }

    private void likeImage() {
        imageFavorite = database.getLikeBySrc(imageFavorites.get(viewPager.getCurrentItem()).getSrc(), true);
        if (checkLike) {
            database.deleteImage(imageFavorite);
            imgLike.setImageResource(R.drawable.ic_like);
            Toast.makeText(DownLoadActivity.this, "Dislike", Toast.LENGTH_SHORT).show();
            checkLike = false;
        } else {
            database.addImage(imageFavorite, true);
            imgLike.setImageResource(R.drawable.ic_liked);
            Toast.makeText(DownLoadActivity.this, "Like", Toast.LENGTH_SHORT).show();
            checkLike = true;
        }
    }
}
