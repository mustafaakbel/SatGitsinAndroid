package com.mustafa.satgitsin.Activitys;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.mustafa.satgitsin.Adapter.AdapterAyarlarListe;
import com.mustafa.satgitsin.Moduller.AyarlarItem;
import com.mustafa.satgitsin.R;

import java.util.ArrayList;
import java.util.List;

public class KullanicAyarlari extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth auth;
    Button buttonCikis;
    Toolbar toolbarAyarlar;
    List<AyarlarItem> listAyarlarItem;
    RecyclerView listViewAyarlar;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_ayarlari);
        tanimla();
        listViewAyarlari();
    }

    void tanimla(){
        buttonCikis = findViewById(R.id.buttonCikis);
        auth = FirebaseAuth.getInstance();
        toolbarAyarlar = findViewById(R.id.toolbarAyarlar);
        listViewAyarlar = findViewById(R.id.listViewAyarlar);
        listAyarlarItem = new ArrayList<AyarlarItem>();



        setSupportActionBar(toolbarAyarlar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonCikis.setOnClickListener(this);
    }
    void listViewAyarlari(){
        AyarlarItem isim = new AyarlarItem(R.drawable.anasayfa_ic_person,"İsim",R.drawable.kullaniciayarlari_ic_sag);
        AyarlarItem mail = new AyarlarItem(R.drawable.kullaniciayarlari_ic_mail,"E-posta",R.drawable.kullaniciayarlari_ic_sag);
        AyarlarItem sifre = new AyarlarItem(R.drawable.kullaniciayarlari_ic_lock,"Şifre",R.drawable.kullaniciayarlari_ic_sag);
        listAyarlarItem.add(isim);
        listAyarlarItem.add(mail);
        listAyarlarItem.add(sifre);

        layoutManager = new LinearLayoutManager(this);
        listViewAyarlar.setLayoutManager(layoutManager);

        AdapterAyarlarListe adapterAyarlarListe = new AdapterAyarlarListe(this,listAyarlarItem);
        listViewAyarlar.setAdapter(adapterAyarlarListe);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonCikis:
                auth.signOut();
                Intent anasayfa = new Intent(this,BaslangicActivity.class);
                startActivity(anasayfa);
                finish();
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent anasayfa = new Intent(KullanicAyarlari.this,MainActivity.class);
            anasayfa.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NavUtils.navigateUpTo(this, anasayfa);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
