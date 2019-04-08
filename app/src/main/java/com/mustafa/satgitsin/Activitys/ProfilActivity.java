package com.mustafa.satgitsin.Activitys;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mustafa.satgitsin.Adapter.AdapterProfilFragment;
import com.mustafa.satgitsin.Moduller.Kullanici;
import com.mustafa.satgitsin.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilActivity extends AppCompatActivity {
    DatabaseReference kullaniciDb;
    String id;
    TextView KarsiProfilIsım,KarsiProfilMail;
    CircleImageView KarsiProfilavatar;
    TabLayout KarsiProfiltabLayoutProfil;
    ViewPager KarsiProfilviewPagerProfil;
    StorageReference mStorageRef;
    AdapterProfilFragment adapterProfilFragment;
    Toolbar toolbarProfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        tanimla();
    }

    void tanimla(){
        kullaniciDb = FirebaseDatabase.getInstance().getReference("Kullanici");
        id = getIntent().getStringExtra("profilId").toString();
        KullaniciBul();
        kullaniciDb.keepSynced(true);
        KarsiProfilIsım = findViewById(R.id.KarsiProfilIsım);
        KarsiProfilMail = findViewById(R.id.KarsiProfilMail);
        KarsiProfilavatar = findViewById(R.id.KarsiProfilavatar);
        KarsiProfiltabLayoutProfil = findViewById(R.id.KarsiProfiltabLayoutProfil);
        KarsiProfilviewPagerProfil = findViewById(R.id.KarsiProfilviewPagerProfil);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        adapterProfilFragment = new AdapterProfilFragment(getSupportFragmentManager(),id);
        KarsiProfilviewPagerProfil.setAdapter(adapterProfilFragment);
        KarsiProfiltabLayoutProfil.setupWithViewPager(KarsiProfilviewPagerProfil);
        toolbarProfil = findViewById(R.id.toolbarProfil);
        toolbarProfil.setTitle("");
        setSupportActionBar(toolbarProfil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void KullaniciBul(){
        kullaniciDb.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                KarsiProfilIsım.setText(kullanici.getIsim());
                KarsiProfilMail.setText(kullanici.getMail());
                //profil Fotosu bulma
                if (kullanici.getProfilFoto().equals("bos")){
                    Glide.with(ProfilActivity.this).load(R.drawable.profil_ic_avatar).into(KarsiProfilavatar);
                }else {
                    Glide.with(ProfilActivity.this).load(kullanici.getProfilFoto()).into(KarsiProfilavatar);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent anasayfa = new Intent(ProfilActivity.this,MainActivity.class);
                anasayfa.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, anasayfa);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
