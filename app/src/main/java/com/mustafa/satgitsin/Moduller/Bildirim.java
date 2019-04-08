package com.mustafa.satgitsin.Moduller;

public class Bildirim {
    String mesaj_id,type;

    public Bildirim(String mesaj_id, String type) {
        this.mesaj_id = mesaj_id;
        this.type = type;
    }

    public Bildirim(){

    }

    public String getMesaj_id() {
        return mesaj_id;
    }

    public void setMesaj_id(String mesaj_id) {
        this.mesaj_id = mesaj_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
