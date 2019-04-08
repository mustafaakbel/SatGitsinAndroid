package com.mustafa.satgitsin.Activitys;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mustafa.satgitsin.Adapter.AdapterAnasayfaFragment;
import com.mustafa.satgitsin.Moduller.Kullanici;
import com.mustafa.satgitsin.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth auth;
    TextView baslangicActivityInfo;
    TabLayout AnaLayout;
    Toolbar AnaToolbar;
    ViewPager viewPagerAna;
    AdapterAnasayfaFragment adapterAnasayfaFragment;
    DatabaseReference db;
    Button setting;
    TextView textViewToolbarIsim;
    String fragmentItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tanimla();
    }
    void tanimla(){
        auth = FirebaseAuth.getInstance();
        AnaLayout = findViewById(R.id.AnaLayout);
        textViewToolbarIsim = findViewById(R.id.textViewToolbarIsim);
        AnaToolbar = findViewById(R.id.AnaToolbar);
        adapterAnasayfaFragment = new AdapterAnasayfaFragment(getSupportFragmentManager());
        viewPagerAna = findViewById(R.id.viewPagerAna);
        setting = findViewById(R.id.setting);
        db = FirebaseDatabase.getInstance().getReference("Kullanici");
        db.keepSynced(true);
        Intent intent = getIntent();
        fragmentItem = intent.getStringExtra("fragmentItem");


        viewPagerAna.setAdapter(adapterAnasayfaFragment);
        AnaLayout.setupWithViewPager(viewPagerAna);
        textViewToolbarIsim.setText("Anasayfa");
        setting.setOnClickListener(this);
        AnaLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#ff7373"), PorterDuff.Mode.SRC_IN);
                if (tab.getPosition() == 0){
                    textViewToolbarIsim.setText("Anasayfa");
                    setting.setVisibility(View.INVISIBLE);
                }else if(tab.getPosition() == 1){
                    textViewToolbarIsim.setText("İlan ver");
                    setting.setVisibility(View.INVISIBLE);
                }else if(tab.getPosition()==2){
                    textViewToolbarIsim.setText("Mesajlar");
                    setSupportActionBar(AnaToolbar);
                    setting.setVisibility(View.INVISIBLE);
                }else if(tab.getPosition()==3){
                    textViewToolbarIsim.setText("Profil");
                    setSupportActionBar(AnaToolbar);
                    setting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        setupTabIcons();
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null){
            Intent BaslangicActivityIntent = new Intent(MainActivity.this,BaslangicActivity.class);
            startActivity(BaslangicActivityIntent);
            finish();
        }
    }
    private void setupTabIcons() {
        AnaLayout.getTabAt(0).setIcon(R.drawable.anasayfa_ic_home);
        AnaLayout.getTabAt(1).setIcon(R.drawable.anasayfa_ic_camera);
        AnaLayout.getTabAt(2).setIcon(R.drawable.anasayfa_ic_message);
        AnaLayout.getTabAt(3).setIcon(R.drawable.anasayfa_ic_person);
        AnaLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#ff7373"), PorterDuff.Mode.SRC_IN);
        //urun satıldıktan sonraki yonlendirme
        if (fragmentItem!=null){
            viewPagerAna.setCurrentItem(Integer.parseInt(fragmentItem));
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.setting:
                Intent ayarlar = new Intent(this,KullanicAyarlari.class);
                startActivity(ayarlar);
                break;
        }
    }

}
