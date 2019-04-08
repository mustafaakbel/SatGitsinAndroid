package com.mustafa.satgitsin.Moduller;

import android.support.v7.widget.RecyclerView;

public class AyarlarItem{
    int image;
    String itemAd;
    int sagOk;
    public AyarlarItem(int image,String itemAd,int sagOk){
        this.image = image;
        this.itemAd = itemAd;
        this.sagOk = sagOk;
    }

    public AyarlarItem() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getItemAd() {
        return itemAd;
    }

    public void setItemAd(String itemAd) {
        this.itemAd = itemAd;
    }

    public int getSagOk() {
        return sagOk;
    }

    public void setSagOk(int sagOk) {
        this.sagOk = sagOk;
    }
}
