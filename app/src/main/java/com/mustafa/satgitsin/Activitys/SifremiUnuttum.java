package com.mustafa.satgitsin.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.mustafa.satgitsin.R;


public class SifremiUnuttum extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth auth;
    EditText editTextSifremiUnuttumMail;
    Toolbar toolbarSifremiUnuttum;
    Button buttonSifremiUnuttum;
    ProgressDialog progressDialog;
    Intent geri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifremi_unuttum);
        tanimla();
    }

    void tanimla(){
        toolbarSifremiUnuttum = findViewById(R.id.toolbarSifremiUnuttum);
        toolbarSifremiUnuttum.setTitle("Şifremi Unuttum");
        setSupportActionBar(toolbarSifremiUnuttum);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        auth = FirebaseAuth.getInstance();
        editTextSifremiUnuttumMail = findViewById(R.id.editTextSifremiUnuttumMail);
        buttonSifremiUnuttum = findViewById(R.id.buttonSifremiUnuttum);
        geri = NavUtils.getParentActivityIntent(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Şifre yenileme");
        progressDialog.setMessage("Şifre yenileme e-postası mailinize gönderiliyor..");
        progressDialog.setCanceledOnTouchOutside(false);
        buttonSifremiUnuttum.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            geri.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NavUtils.navigateUpTo(this, geri);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSifremiUnuttum:
                    progressDialog.show();
                    auth.sendPasswordResetEmail(editTextSifremiUnuttumMail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(SifremiUnuttum.this,"Yenileme e-postası gönderildi",Toast.LENGTH_SHORT).show();
                                geri.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                NavUtils.navigateUpTo(SifremiUnuttum.this, geri);
                            }else{
                                progressDialog.hide();
                                Toast.makeText(SifremiUnuttum.this,"E-posta gönderilirken hata oluştu",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                break;
                }
        }
    }

