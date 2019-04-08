package com.mustafa.satgitsin.Moduller;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

public class Urun implements Serializable {
    String urunId;
    private String kategoriAdi;
    private String urunBaslik;
    private String urunOzellikleri;
    private int urunFiyat;
    private String uid;
    private String satilmaDurumu;
    private String  urunFoto;
    private String urunFotoAdet;
    public Urun(String urunId,String kategoriAdi,String urunBaslik,String urunOzellikleri, int urunFiyat,String uid,String urunFoto,String urunFotoAdet){
        this.urunId=urunId;
        this.kategoriAdi = kategoriAdi;
        this.urunBaslik=urunBaslik;
        this.urunOzellikleri = urunOzellikleri;
        this.urunFiyat = urunFiyat;
        this.uid = uid;
        satilmaDurumu="0";
        this.urunFoto=urunFoto;
        this.urunFotoAdet=urunFotoAdet;
    }


    public String getUrunFotoAdet() {
        return urunFotoAdet;
    }

    public void setUrunFotoAdet(String urunFotoAdet) {
        this.urunFotoAdet = urunFotoAdet;
    }

    public String getUrunFoto() {
        return urunFoto;
    }

    public void setUrunFoto(String urunFoto) {
        this.urunFoto = urunFoto;
    }

    public Urun(){
    }

    public String getSatilmaDurumu() {
        return satilmaDurumu;
    }

    public void setSatilmaDurumu(String satilmaDurumu) {
        this.satilmaDurumu = satilmaDurumu;
    }

    public String getUrunId() {
        return urunId;
    }

    public void setUrunId(String urunId) {
        this.urunId = urunId;
    }

    public String getUrunBaslik() {
        return urunBaslik;
    }

    public void setUrunBaslik(String urunBaslik) {
        this.urunBaslik = urunBaslik;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKategoriAdi() {
        return kategoriAdi;
    }

    public void setKategoriAdi(String kategoriAdi) {
        this.kategoriAdi = kategoriAdi;
    }

    public String getUrunOzellikleri() {
        return urunOzellikleri;
    }

    public void setUrunOzellikleri(String urunOzellikleri) {
        this.urunOzellikleri = urunOzellikleri;
    }

    public int getUrunFiyat() {
        return urunFiyat;
    }

    public void setUrunFiyat(int urunFiyat) {
        this.urunFiyat = urunFiyat;
    }
}
