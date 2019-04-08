package com.mustafa.satgitsin.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mustafa.satgitsin.Fragmentler.SatiliyorFragment;
import com.mustafa.satgitsin.Fragmentler.SatildiFragment;
import com.mustafa.satgitsin.Fragmentler.YorumFragment;

public class AdapterProfilFragment extends FragmentPagerAdapter {
    String id;
    public AdapterProfilFragment(FragmentManager fm,String id) {

        super(fm);
        this.id=id;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                SatiliyorFragment satiliyorFragment = new SatiliyorFragment(id);
                return satiliyorFragment;
            case 1:
                SatildiFragment satildiFragment = new SatildiFragment(id);
                return satildiFragment;
            case 2:
                YorumFragment yorumFragment = new YorumFragment(id);
                return yorumFragment;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Satılıyor";
            case 1:
                return "Satıldı";
            case 2:
                return "Yorumlar";
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
