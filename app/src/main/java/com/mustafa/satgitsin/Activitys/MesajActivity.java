package com.mustafa.satgitsin.Activitys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mustafa.satgitsin.Adapter.AdapterMesaj;
import com.mustafa.satgitsin.Moduller.Bildirim;
import com.mustafa.satgitsin.Moduller.Kullanici;
import com.mustafa.satgitsin.Moduller.Mesaj;
import com.mustafa.satgitsin.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MesajActivity extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbarMesajGonder;
    CircleImageView mesajSayfasiAvatar;
    TextView mesajSayfasiIsim;
    DatabaseReference db;
    String kisiId;
    EditText mesajIcerik;
    ImageView imageViewGonder;
    List<Mesaj> mesajlar;
    RecyclerView recylerViewMesajlar;
    AdapterMesaj adapterMesaj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaj);
        tanimla();
    }

    void tanimla(){
        mesajSayfasiAvatar = findViewById(R.id.mesajSayfasiAvatar);
        mesajSayfasiIsim = findViewById(R.id.mesajSayfasiIsim);
        toolbarMesajGonder = findViewById(R.id.toolbarMesajGonder);
        imageViewGonder = findViewById(R.id.imageViewGonder);
        mesajIcerik = findViewById(R.id.mesajIcerik);
        recylerViewMesajlar = findViewById(R.id.recylerViewMesajlar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recylerViewMesajlar.setLayoutManager(linearLayoutManager);
        kisiId = getIntent().getStringExtra("kisiId");
        db = FirebaseDatabase.getInstance().getReference();
        db.keepSynced(true);
        setSupportActionBar(toolbarMesajGonder);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        KullaniciBul();
        imageViewGonder.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent anasayfa = new Intent(MesajActivity.this,MainActivity.class);
                anasayfa.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, anasayfa);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void KullaniciBul(){

        db.child("Kullanici").child(kisiId.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                mesajSayfasiIsim.setText(kullanici.getIsim().toString());
                mesajSayfasiIsim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getId()==mesajSayfasiIsim.getId()){
                            Intent profil = new Intent(getBaseContext(),ProfilActivity.class);
                            profil.putExtra("profilId",kisiId);
                            startActivity(profil);
                        }
                    }
                });
                if (!kullanici.getProfilFoto().equals("bos")){
                    Glide.with(getBaseContext()).load(kullanici.getProfilFoto()).into(mesajSayfasiAvatar);
                }else{
                    Glide.with(getBaseContext()).load(R.drawable.profil_ic_avatar).into(mesajSayfasiAvatar);
                }
                MesajlariCek(kullanici.getProfilFoto());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void MesajlariCek(final String profilFoto){
        mesajlar = new ArrayList<>();
        db.child("Mesajlar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            mesajlar.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    Mesaj mesaj = data.getValue(Mesaj.class);
                    if (mesaj.getGonderenKisiId().equals(kisiId.toString()) && mesaj.getAliciKisiId().equals(FirebaseAuth.getInstance().getUid()) ||
                            mesaj.getGonderenKisiId().equals(FirebaseAuth.getInstance().getUid()) && mesaj.getAliciKisiId().equals(kisiId.toString())){
                        mesajlar.add(mesaj);
                    }


                }
                adapterMesaj = new AdapterMesaj(MesajActivity.this,mesajlar,profilFoto);
                recylerViewMesajlar.scrollToPosition(mesajlar.size()-1);
                recylerViewMesajlar.setAdapter(adapterMesaj);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageViewGonder:
                if (!TextUtils.isEmpty(mesajIcerik.getText().toString())){
                    final Mesaj mesaj = new Mesaj(mesajIcerik.getText().toString(), FirebaseAuth.getInstance().getUid(),kisiId.toString());
                    final String id = db.push().getKey();
                    db.child("Mesajlar").child(id).setValue(mesaj).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Bildirim bildirim = new Bildirim(id,"mesaj");
                                String a=FirebaseAuth.getInstance().getUid();
                                String randomId = db.push().getKey();
                                db.child("Bildirimler").child(a).child(randomId).setValue(bildirim);
                            }
                        }
                    });
                    final DatabaseReference mesajListe = FirebaseDatabase.getInstance().getReference("MesajListe");
                    mesajListe.child(FirebaseAuth.getInstance().getUid()).child(kisiId.toString())
                            .child("id").setValue(kisiId.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mesajListe.child(kisiId.toString()).child(FirebaseAuth.getInstance().getUid())
                                        .child("id").setValue(FirebaseAuth.getInstance().getUid());

                            }

                        }
                    });

                  /* mesajListe.child(FirebaseAuth.getInstance().getUid()).child(kisiId.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()){
                                mesajListe.child("id").setValue(kisiId.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        mesajListe.child(kisiId.toString()).child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (!dataSnapshot.exists()){
                                                    mesajListe.child("id").setValue(FirebaseAuth.getInstance().getUid());
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/
                    mesajIcerik.setText("");

                }else{
                    Toast.makeText(this, "Boş mesaj atamazsınız..", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
