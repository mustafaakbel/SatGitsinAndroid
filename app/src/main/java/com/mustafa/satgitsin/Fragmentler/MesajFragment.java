package com.mustafa.satgitsin.Fragmentler;


import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.mustafa.satgitsin.Adapter.AdapterMesajlasilanKullanicilar;
import com.mustafa.satgitsin.Moduller.Kullanici;
import com.mustafa.satgitsin.Moduller.Mesaj;
import com.mustafa.satgitsin.Moduller.MesajListe;
import com.mustafa.satgitsin.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MesajFragment extends Fragment {
    RecyclerView mesajlasilanKisiler;
    View view;
    List<MesajListe> mesajListes;
    List<Kullanici> kullanicilar;
    DatabaseReference db,kullanicidb;
    AdapterMesajlasilanKullanicilar adapterMesajlasilanKullanicilar;
    public MesajFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mesaj, container, false);
        tanimla();
        return view;
    }

    void tanimla(){
        mesajlasilanKisiler = view.findViewById(R.id.mesajlasilanKisiler);
        db = FirebaseDatabase.getInstance().getReference("MesajListe").child(FirebaseAuth.getInstance().getUid());
        kullanicidb = FirebaseDatabase.getInstance().getReference("Kullanici");
        db.keepSynced(true);
        mesajlasilanKisiler.setLayoutManager(new LinearLayoutManager(getContext()));
        mesajListes = new ArrayList<>();
        kullanicilar = new ArrayList<>();
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mesajListes.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    MesajListe mesajListe = data.getValue(MesajListe.class);
                    mesajListes.add(mesajListe);
                }
                Log.d("aaaaaa",String.valueOf(dataSnapshot.getKey()));
                kullanicilariBul();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void kullanicilariBul() {
        kullanicidb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kullanicilar.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Kullanici kullanici = data.getValue(Kullanici.class);
                    for(MesajListe mesajliste : mesajListes){
                        if (kullanici.getUid().equals(mesajliste.getId())){
                            kullanicilar.add(kullanici);
                        }
                    }

                }
                AdapterMesajlasilanKullanicilar adapterMesajlasilanKullanicilar = new AdapterMesajlasilanKullanicilar(getContext(),kullanicilar);
                mesajlasilanKisiler.setAdapter(adapterMesajlasilanKullanicilar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
