package com.mustafa.satgitsin.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mustafa.satgitsin.Fragmentler.AnasayfaFragment;
import com.mustafa.satgitsin.Fragmentler.IlanverFragment;
import com.mustafa.satgitsin.Fragmentler.ProfilFragment;
import com.mustafa.satgitsin.Fragmentler.MesajFragment;

public class AdapterAnasayfaFragment extends FragmentPagerAdapter {


    public AdapterAnasayfaFragment(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                AnasayfaFragment anasayfaFragment = new AnasayfaFragment();
                return anasayfaFragment;
            case 1:
                IlanverFragment ilanverFragment = new IlanverFragment();
                return ilanverFragment;
            case 2:
                MesajFragment mesajFragment = new MesajFragment();
                return mesajFragment;
            case 3:
                ProfilFragment profilFragment = new ProfilFragment();
                return profilFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }



}
