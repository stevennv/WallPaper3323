package com.scompany.wallpaper.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Admin on 2/28/2018.
 */

public class CommonUtil {
    public static void shareApp(Activity context) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + context.getPackageName());
        context.startActivity(Intent.createChooser(sharingIntent, "Choose one"));
    }

}
