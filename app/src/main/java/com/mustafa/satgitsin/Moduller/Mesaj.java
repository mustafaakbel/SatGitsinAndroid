package com.mustafa.satgitsin.Moduller;

public class Mesaj {
    String mesajIcerik,gonderenKisiId,aliciKisiId;

    public Mesaj(String mesajIcerik, String gonderenKisiId, String aliciKisiId) {
        this.mesajIcerik = mesajIcerik;
        this.gonderenKisiId = gonderenKisiId;
        this.aliciKisiId = aliciKisiId;
    }
    public Mesaj(){

    }
    public String getMesajIcerik() {
        return mesajIcerik;
    }

    public void setMesajIcerik(String mesajIcerik) {
        this.mesajIcerik = mesajIcerik;
    }

    public String getGonderenKisiId() {
        return gonderenKisiId;
    }

    public void setGonderenKisiId(String gonderenKisiId) {
        this.gonderenKisiId = gonderenKisiId;
    }

    public String getAliciKisiId() {
        return aliciKisiId;
    }

    public void setAliciKisiId(String aliciKisiId) {
        this.aliciKisiId = aliciKisiId;
    }
}
