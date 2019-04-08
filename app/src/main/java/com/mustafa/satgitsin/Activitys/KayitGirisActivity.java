package com.mustafa.satgitsin.Activitys;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.mustafa.satgitsin.Adapter.AdapterKayitGirisFragment;
import com.mustafa.satgitsin.R;

public class KayitGirisActivity extends AppCompatActivity {

    Toolbar FragmentToolbar;
    TabLayout topLayout;
    private ViewPager viewPagerGirisKayit;
    private AdapterKayitGirisFragment adapterKayitGirisFragment;
    Bundle tusidBundle;
    int tusid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_giris);
        tanimla();
        FragmentAyarlari();
        ToolBarAyarlari();
    }
    void tanimla(){
        FragmentToolbar = findViewById(R.id.FragmentToolbar);
        topLayout = findViewById(R.id.topLayout);
        viewPagerGirisKayit = findViewById(R.id.viewPagerGirisKayit);
        adapterKayitGirisFragment = new AdapterKayitGirisFragment(getSupportFragmentManager());
    }
    void FragmentAyarlari(){
        viewPagerGirisKayit.setAdapter(adapterKayitGirisFragment);
        viewPagerGirisKayit.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                int pageSayfasi = viewPagerGirisKayit.getCurrentItem();
                if(pageSayfasi == 0){
                    FragmentToolbar.setTitle("Yeni Hesap Oluştur");
                }else if(pageSayfasi==1){
                    FragmentToolbar.setTitle("Giriş Yap");
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        topLayout.setupWithViewPager(viewPagerGirisKayit);
        tusidBundle = getIntent().getExtras();
        tusid = tusidBundle.getInt("tusid");
        if ( R.id.baslangicActivityGirisYap == tusid){
            FragmentToolbar.setTitle("Giriş Yap");
            viewPagerGirisKayit.setCurrentItem(2);
        }else if (R.id.baslangicActivityGirisYap != tusid){
            FragmentToolbar.setTitle("Yeni Hesap Oluştur");
        }
    }
    void ToolBarAyarlari(){
        setSupportActionBar(FragmentToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
