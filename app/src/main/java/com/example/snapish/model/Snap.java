package com.example.snapish.model;

import android.graphics.Bitmap;

public class Snap {
    private String id;
    private String text;
    private Bitmap snapBitmap;

    public Snap(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getSnapBitmap() {
        return snapBitmap;
    }

    public void setSnapBitmap(Bitmap snapBitmap) {
        this.snapBitmap = snapBitmap;
    }
}
