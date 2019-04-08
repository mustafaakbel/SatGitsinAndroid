package com.mustafa.satgitsin.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mustafa.satgitsin.R;

public class BaslangicActivity extends AppCompatActivity implements View.OnClickListener {

    Button baslangicActivityKaydol,baslangicActivityGirisYap;
    Bundle hangiTus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baslangic);
        tanimla();
    }
    void tanimla(){
        baslangicActivityKaydol = findViewById(R.id.baslangicActivityKaydol);
        baslangicActivityGirisYap = findViewById(R.id.baslangicActivityGirisYap);
        baslangicActivityKaydol.setOnClickListener(this);
        baslangicActivityGirisYap.setOnClickListener(this);
        hangiTus = new Bundle();
    }

    @Override
    public void onClick(View view) {
        Intent yonlendir = new Intent(BaslangicActivity.this,KayitGirisActivity.class);
        switch (view.getId()){
            case R.id.baslangicActivityKaydol:
                yonlendir.putExtra("tusid",baslangicActivityKaydol.getId());
                startActivity(yonlendir);
                break;
            case R.id.baslangicActivityGirisYap:
                yonlendir.putExtra("tusid",baslangicActivityGirisYap.getId());
                startActivity(yonlendir);
                break;
        }

    }
}
