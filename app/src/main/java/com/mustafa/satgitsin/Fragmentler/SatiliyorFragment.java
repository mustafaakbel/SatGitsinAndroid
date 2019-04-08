package com.mustafa.satgitsin.Fragmentler;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mustafa.satgitsin.Adapter.AdapterProfilFragment;
import com.mustafa.satgitsin.Adapter.AdapterUrunListeleme;
import com.mustafa.satgitsin.Moduller.Urun;
import com.mustafa.satgitsin.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SatiliyorFragment extends Fragment {

    DatabaseReference urunlerimDb;
    View view;
    List<Urun> urunler;
    AdapterUrunListeleme adapterUrunListeleme;
    RecyclerView listViewSatilanUrunler;
    RecyclerView.LayoutManager layoutManager;
    FirebaseAuth auth;
    String id;
    @SuppressLint("ValidFragment")
    public SatiliyorFragment(String id) {
        this.id=id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_satiliyor, container, false);
        tanimla();
        urunCek();
        return view;
    }


    void tanimla(){
        urunlerimDb = FirebaseDatabase.getInstance().getReference("Urunler");
        urunlerimDb.keepSynced(true);
        urunler=new ArrayList<>();
        listViewSatilanUrunler = view.findViewById(R.id.listViewSatilanUrunler);
        layoutManager = new LinearLayoutManager(getContext());
        listViewSatilanUrunler.setLayoutManager(layoutManager);
        auth = FirebaseAuth.getInstance();

        urunlerimDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    Urun urun = data.getValue(Urun.class);
                    if (id.equals("")){
                        if (urun.getUid().equals(auth.getUid()) && urun.getSatilmaDurumu().equals("0")){
                            urunler.add(urun);
                        }
                    }else if(urun.getUid().equals(id) && urun.getSatilmaDurumu().equals("0")){
                        urunler.add(urun);
                    }

                }
                adapterUrunListeleme=new AdapterUrunListeleme(getContext(),urunler);
                listViewSatilanUrunler.setAdapter(adapterUrunListeleme);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //yap burayı
    void urunCek(){

    }

}
