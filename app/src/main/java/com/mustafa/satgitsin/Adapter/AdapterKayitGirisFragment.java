package com.mustafa.satgitsin.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mustafa.satgitsin.Fragmentler.GirisFragment;
import com.mustafa.satgitsin.Fragmentler.KayitFragment;

public class AdapterKayitGirisFragment extends FragmentPagerAdapter {

    public AdapterKayitGirisFragment(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                KayitFragment kayitFragment = new KayitFragment();
                return kayitFragment;
            case 1:
                GirisFragment girisFragment = new GirisFragment();
                return girisFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Kaydol";
            case 1:
                return "Giriş Yap";
        }
        return super.getPageTitle(position);
    }


}
