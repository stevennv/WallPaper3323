package com.scompany.wallpaper.model;

import java.io.Serializable;

/**
 * Created by Admin on 2/28/2018.
 */

public class Data implements Serializable{
    private Images[] Images;
    private String Name;
    private int Count;

    public Images[] getImages() {
        return Images;
    }

    public void setImages(Images[] images) {
        this.Images = images;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
