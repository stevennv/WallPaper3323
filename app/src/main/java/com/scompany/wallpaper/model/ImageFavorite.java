package com.scompany.wallpaper.model;

/**
 * Created by Admin on 3/1/2018.
 */

public class ImageFavorite {
    private int id;
    private String name;
    private String src;

    public ImageFavorite(int id, String name, String src) {
        this.id = id;
        this.name = name;
        this.src = src;
    }

    public ImageFavorite(String name, String src) {
        this.name = name;
        this.src = src;
    }

    public ImageFavorite() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
