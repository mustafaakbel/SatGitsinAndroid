package com.mustafa.satgitsin.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mustafa.satgitsin.Adapter.AdapterSlider;
import com.mustafa.satgitsin.Moduller.Kullanici;
import com.mustafa.satgitsin.Moduller.Urun;
import com.mustafa.satgitsin.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UrunDetay extends AppCompatActivity implements View.OnClickListener{
    Urun urun;
    DatabaseReference db,kullaniciDb;
    Toolbar toolbarUrunDetay;
    ViewPager viewPagerUrunFotolar;
    AdapterSlider adapterSlider;
    TextView textViewurunAdi,textViewUrunDetayFiyat,textViewUrunOzellikleri;
    Button buttonSatildi,mesajGonder;
    FirebaseAuth auth;
    CircleImageView urunDetayImageView;
    StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_detay);
        tanimla();
        urunDetayAyarlama();
    }
    void tanimla(){
        urun = (Urun) getIntent().getSerializableExtra("urun");
        auth = FirebaseAuth.getInstance();
        db= FirebaseDatabase.getInstance().getReference("Urunler");
        kullaniciDb= FirebaseDatabase.getInstance().getReference("Kullanici");
        db.keepSynced(true);
        kullaniciDb.keepSynced(true);
        toolbarUrunDetay = findViewById(R.id.toolbarUrunDetay);
        toolbarUrunDetay.inflateMenu(R.menu.urundetaymenu);
        viewPagerUrunFotolar = findViewById(R.id.viewPagerUrunFotolar);
        adapterSlider = new AdapterSlider(this,urun);
        textViewurunAdi = findViewById(R.id.textViewurunAdi);
        textViewUrunDetayFiyat = findViewById(R.id.textViewUrunDetayFiyat);
        textViewUrunOzellikleri = findViewById(R.id.textViewUrunOzellikleri);
        buttonSatildi = findViewById(R.id.buttonSatildi);
        mesajGonder = findViewById(R.id.mesajGonder);
        urunDetayImageView = findViewById(R.id.urunDetayImageView);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        viewPagerUrunFotolar.setAdapter(adapterSlider);
        toolbarUrunDetay.setTitle("");
        setSupportActionBar(toolbarUrunDetay);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (urun.getSatilmaDurumu().equals("1")){
            buttonSatildi.setText("Bu ürün satılmıştır.");
        }

        buttonSatildi.setOnClickListener(this);
        urunDetayImageView.setOnClickListener(this);
        if (urun.getUid().equals(auth.getUid())){
            buttonSatildi.setVisibility(View.VISIBLE);
        }else{
            mesajGonder.setVisibility(View.VISIBLE);
        }
        mesajGonder.setOnClickListener(this);
        profilFoto();
    }
    //urun ayarlama
    void urunDetayAyarlama(){
        textViewurunAdi.setText(urun.getUrunBaslik().toString());
        textViewUrunDetayFiyat.setText(String.valueOf(urun.getUrunFiyat()));
        textViewUrunOzellikleri.setText(String.valueOf(urun.getUrunOzellikleri()));
    }
    //profil fotosunu çekme
    void profilFoto(){
        kullaniciDb.child(urun.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                if (!kullanici.getProfilFoto().equals("bos")){
                    Glide.with(UrunDetay.this).load(kullanici.getProfilFoto()).into(urunDetayImageView);
                }else{
                    Glide.with(UrunDetay.this).load(R.drawable.profil_ic_avatar).into(urunDetayImageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    //urun silme işlemi
    void urunSilme(){
        db.child(urun.getUrunId()).removeValue();
        for (int i=0;i<Integer.parseInt(urun.getUrunFotoAdet());i++) {
            mStorageRef.child("Urunler/").child(urun.getUrunId()).child("item"+i).delete();
        }

    }
    //menu ayarlama
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (urun.getUid().equals(auth.getUid())){
            getMenuInflater().inflate(R.menu.urundetaymenu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent anasayfa = new Intent(UrunDetay.this,MainActivity.class);
                anasayfa.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, anasayfa);
                break;
            case R.id.IlanıSil:
                AlertDialog.Builder alertDialog =new AlertDialog.Builder(this);
                alertDialog.setTitle("SatGitsin");
                alertDialog.setMessage("Bu ürünü silmek istediğinizden emin misiniz  ?");
                alertDialog.setPositiveButton("EVET", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        urunSilme();
                        Intent kendiProfili = new Intent(UrunDetay.this,MainActivity.class);
                        kendiProfili.putExtra("fragmentItem","3");
                        startActivity(kendiProfili);
                    }
                });
                alertDialog.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.show();
                break;
            case R.id.IlaniDuzenle:
                Intent ilanDuzenle = new Intent(this,IlanDuzenleActivity.class);
                ilanDuzenle.putExtra("urun",urun);
                startActivity(ilanDuzenle);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void SatilmaDurumu(){
        if (urun.getSatilmaDurumu().equals("0")){
            AlertDialog.Builder alertDialog =new AlertDialog.Builder(this);
            alertDialog.setTitle("SatGitsin");
            alertDialog.setMessage("Bu ürünü para karşılığında sattığınızdan emin misiniz ?");
            alertDialog.setPositiveButton("EVET", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    db.child(urun.getUrunId()).child("satilmaDurumu").setValue("1");
                    Intent profil=new Intent(UrunDetay.this,MainActivity.class);
                    profil.putExtra("fragmentItem","3");
                    startActivity(profil);
                }
            });
            alertDialog.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSatildi:
                SatilmaDurumu();
                break;
            case R.id.urunDetayImageView:
                if (auth.getUid().equals(urun.getUid())){
                    Intent kendiProfili = new Intent(UrunDetay.this,MainActivity.class);
                    kendiProfili.putExtra("fragmentItem","3");
                    startActivity(kendiProfili);
                }else{
                    Intent profil = new Intent(UrunDetay.this,ProfilActivity.class);
                    profil.putExtra("profilId",urun.getUid());
                    startActivity(profil);
                }

                break;
            case R.id.mesajGonder:
                Intent mesajSayfasi = new Intent(this, MesajActivity.class);
                mesajSayfasi.putExtra("kisiId",urun.getUid());
                startActivity(mesajSayfasi);
                break;
        }
    }
}
