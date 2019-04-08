package com.mustafa.satgitsin.Moduller;

public class Kullanici {
    String uid,mail,sifre,isim,profilFoto,token;

    public Kullanici(String uid,String mail,String sifre,String isim,String profilFoto,String token) {
        this.uid = uid;
        this.mail = mail;
        this.sifre = sifre;
        this.isim = isim;
        this.profilFoto=profilFoto;
        this.token=token;
    }

    public Kullanici() {
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfilFoto() {
        return profilFoto;
    }

    public void setProfilFoto(String profilFoto) {
        this.profilFoto = profilFoto;
    }

    public String getUid() {
        return uid;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }
}
