package com.scompany.wallpaper.model;

/**
 * Created by Admin on 3/1/2018.
 */

public class ImageFavorite {
    private int id;
    private String name;
    private String src;
    private boolean isLike;

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public ImageFavorite(int id, String name, String src) {
        this.id = id;
        this.name = name;
        this.src = src;
    }

    public ImageFavorite(String name, String src,boolean isLike) {
        this.name = name;
        this.src = src;
        this.isLike = isLike;
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
