package com.mustafa.satgitsin.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mustafa.satgitsin.Moduller.Kullanici;
import com.mustafa.satgitsin.R;

import java.util.regex.Pattern;

public class AyarDegistirme extends AppCompatActivity implements View.OnClickListener {
    FirebaseUser user;
    DatabaseReference db;
    Toolbar toolbarAyarDegistir;
    EditText editTextMetin;
    Button buttonKullaniciAyarlariKaydet;
    Kullanici kullanici;
    TextView textViewEposta;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayar_degistirme);
        toolbarAyarDegistir = findViewById(R.id.toolbarAyarDegistir);
        final String listeadi = getIntent().getStringExtra("listeadi");

        if (listeadi.equals("Fotoğraf")){
            Toast.makeText(this, "eşit", Toast.LENGTH_SHORT).show();
            toolbarAyarDegistir.setTitle(listeadi+" değiştir");
        }else if(listeadi.equals("İsim")){
            toolbarAyarDegistir.setTitle(listeadi+" değiştir");

        }else if(listeadi.equals("E-posta")){
            toolbarAyarDegistir.setTitle(listeadi+" değiştir");
        }else if(listeadi.equals("Şifre")){
            toolbarAyarDegistir.setTitle(listeadi+" değiştir");
        }
        tanimla();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference("Kullanici").child(user.getUid());
        db.keepSynced(true);
        textViewEposta = findViewById(R.id.textViewEposta);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                kullanici = dataSnapshot.getValue(Kullanici.class);
                if(listeadi.equals("İsim")){
                    textViewEposta.setVisibility(View.INVISIBLE);
                    editTextMetin.setText(kullanici.getIsim());
                }else if(listeadi.equals("E-posta")){
                    editTextMetin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS  );
                    textViewEposta.setVisibility(View.VISIBLE);
                    textViewEposta.setText("ŞU ANKİ E-POSTAN : " +kullanici.getMail());
                    editTextMetin.setHint("Mail giriniz..");
                }else if(listeadi.equals("Şifre")){
                    textViewEposta.setVisibility(View.INVISIBLE);
                    editTextMetin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD  );
                    editTextMetin.setHint("Şifre giriniz..");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    void tanimla(){
        editTextMetin = findViewById(R.id.editTextMetin);
        buttonKullaniciAyarlariKaydet = findViewById(R.id.buttonKullaniciAyarlariKaydet);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Veri Güncelleme");
        progressDialog.setMessage("Veri güncelleniyor lütfen bekleyiniz..");
        progressDialog.setCanceledOnTouchOutside(false);
        setSupportActionBar(toolbarAyarDegistir);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonKullaniciAyarlariKaydet.setOnClickListener(this);
    }
    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    public void onClick(View view) {
        final String elemanid = getIntent().getStringExtra("listeadi");
        final String deger = editTextMetin.getText().toString();
        switch (view.getId()){
            case R.id.buttonKullaniciAyarlariKaydet:

                progressDialog.show();
                if (elemanid.equals("İsim")){
                    db.child("isim").setValue(deger).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Intent geri = new Intent(AyarDegistirme.this,KullanicAyarlari.class);
                                startActivity(geri);
                            }else{
                                progressDialog.hide();
                                Toast.makeText(AyarDegistirme.this, "Veri güncellenirken hata oluştu.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }else if (elemanid.equals("E-posta")){

                    if (Patterns.EMAIL_ADDRESS.matcher(deger).matches()){
                        user.updateEmail(deger).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    db.child("mail").setValue(deger).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.dismiss();
                                                Intent geri = new Intent(AyarDegistirme.this,KullanicAyarlari.class);
                                                startActivity(geri);
                                            }else{
                                                progressDialog.hide();
                                                Toast.makeText(AyarDegistirme.this, "Veri güncellenirken hata oluştu.", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                                }else{
                                    progressDialog.hide();
                                    Toast.makeText(AyarDegistirme.this, "Veri güncellenirken hata .", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        progressDialog.hide();
                        Toast.makeText(AyarDegistirme.this, "Lütfen geçerli mail giriniz..", Toast.LENGTH_SHORT).show();
                    }

                }else if (elemanid.equals("Şifre")){
                    user.updatePassword(editTextMetin.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                db.child("sifre").setValue(editTextMetin.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            progressDialog.dismiss();
                                            Intent geri = new Intent(AyarDegistirme.this,KullanicAyarlari.class);
                                            startActivity(geri);
                                        }else {
                                            progressDialog.hide();
                                            Toast.makeText(AyarDegistirme.this, "Veri güncellenirken hata oluştu.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                progressDialog.hide();
                                Toast.makeText(AyarDegistirme.this, "Veri güncellenirken hata oluştu.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
        }
    }
}
