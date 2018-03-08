package com.scompany.wallpaper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.scompany.wallpaper.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Admin on 3/8/2018.
 */

public class InfoDialog extends Dialog {
    //    private TextView tvContent;
    private WebView webView;
    private ImageView imgClose;

    public InfoDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info);
        webView = findViewById(R.id.tv_content);
        imgClose = findViewById(R.id.img_close);
        try {
            InputStream is = getContext().getAssets().open("info.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String html = new String(buffer);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
