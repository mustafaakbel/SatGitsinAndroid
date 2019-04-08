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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mustafa.satgitsin.Adapter.AdapterUrunListeleme;
import com.mustafa.satgitsin.Moduller.Urun;
import com.mustafa.satgitsin.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SatildiFragment extends Fragment {

    View view;
    DatabaseReference db;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView profilSatilanurun;
    List<Urun> urunler;
    AdapterUrunListeleme adapterUrunListeleme;
    String id;
    @SuppressLint("ValidFragment")
    public SatildiFragment(String id) {
        this.id=id;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_satildi, container, false);
        tanimla();
        urunListele();
        return view;
    }
    void tanimla(){
        db= FirebaseDatabase.getInstance().getReference("Urunler");
        profilSatilanurun = view.findViewById(R.id.profilSatilanurun);
        urunler = new ArrayList<>();
        db.keepSynced(true);
        layoutManager = new LinearLayoutManager(getContext());
        profilSatilanurun.setLayoutManager(layoutManager);
    }
    void urunListele(){
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                urunler.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    Urun urun = data.getValue(Urun.class);
                    if(id.equals("")){
                        if (urun.getUid().equals(FirebaseAuth.getInstance().getUid()) && urun.getSatilmaDurumu().equals("1")){
                            urunler.add(urun);
                        }
                    }else if(urun.getUid().equals(id) && urun.getSatilmaDurumu().equals("1")){
                        urunler.add(urun);
                    }


                }
                adapterUrunListeleme = new AdapterUrunListeleme(getContext(), urunler);
                profilSatilanurun.setAdapter(adapterUrunListeleme);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
