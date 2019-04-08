package com.mustafa.satgitsin.Moduller;

public class Yorum {
    String icerikYorum;
    String id;

    public Yorum(){

    }

    public Yorum(String icerikYorum,String id){
        this.icerikYorum=icerikYorum;
        this.id=id;
    }

    public String getIcerikYorum() {
        return icerikYorum;
    }

    public void setIcerikYorum(String icerikYorum) {
        this.icerikYorum = icerikYorum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
