package com.mustafa.satgitsin.Fragmentler;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mustafa.satgitsin.Adapter.AdapterUrunListeleme;
import com.mustafa.satgitsin.Moduller.Urun;
import com.mustafa.satgitsin.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnasayfaFragment extends Fragment implements View.OnClickListener{

    View view;
    RecyclerView lisViewUrunler;

    DatabaseReference urundb;
    DatabaseReference kategoriDb;
    List<String> urunIdler;
    List<Urun> urunler;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout anasayfaLinearLayout;
    View kategoriView;
    AdapterUrunListeleme adapterUrunListeleme=null;
    LayoutInflater layoutInflater;
    LinearLayout linearLayoutKategoriGoster;
    Button buttonKategoriTemizle;
    TextView textViewKategoriAdGoster;
    String katId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater;
        view = inflater.inflate(R.layout.fragment_anasayfa, container, false);
        tanimla();
        urunIdCekme(katId,"");
        kategoriListele();
        return view;
    }
    void tanimla(){
        lisViewUrunler = view.findViewById(R.id.lisViewUrunler);
        urunIdler = new ArrayList<>();
        urundb = FirebaseDatabase.getInstance().getReference("Urunler");
        kategoriDb = FirebaseDatabase.getInstance().getReference("Kategoriler");
        linearLayoutKategoriGoster = view.findViewById(R.id.linearLayoutKategoriGoster);
        buttonKategoriTemizle= view.findViewById(R.id.buttonKategoriTemizle);
        textViewKategoriAdGoster= view.findViewById(R.id.textViewKategoriAdGoster);
        urundb.keepSynced(true);
        kategoriDb.keepSynced(true);
        urunler = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        lisViewUrunler.setLayoutManager(layoutManager);
        anasayfaLinearLayout = view.findViewById(R.id.anasayfaLinearLayout);
        buttonKategoriTemizle.setOnClickListener(this);
    }
    void urunIdCekme(final String katId, final String KategoriAd){
        urundb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                urunler.clear();
                //urunleri çekme
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    Urun urun = data.getValue(Urun.class);
                    if(katId!=null) {
                        linearLayoutKategoriGoster.setVisibility(View.VISIBLE);
                        textViewKategoriAdGoster.setText(KategoriAd);
                        if (urun.getSatilmaDurumu().equals("0") && urun.getKategoriAdi().equals(katId.toString())) {
                            urunler.add(urun);
                        }
                    }else if(urun.getSatilmaDurumu().equals("0")){
                        linearLayoutKategoriGoster.setVisibility(View.INVISIBLE);
                        urunler.add(urun);
                    }
                }
                adapterUrunListeleme = new AdapterUrunListeleme(getContext(), urunler);
                lisViewUrunler.setAdapter(adapterUrunListeleme);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void kategoriListele(){
        final int[] icon = {R.mipmap.kategori_araba_icon_round,R.mipmap.kategori_emlak_icon_round,R.mipmap.kategori_hizmetler_icon_round,
                      R.mipmap.kategori_elektronik_icon_round,R.mipmap.kategori_evbahce_icon_round,R.mipmap.kategori_spor_icon_round,
                      R.mipmap.kategori_digeraraclar_icon_round,R.mipmap.kategori_giyim_icon_round,R.mipmap.kategori_filmkitap_icon_round,
                      R.mipmap.kategori_bebekcocuk_icon_round,R.mipmap.kategori_diger_icon_round};
        final int[] count = {-1};
        kategoriDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot data:dataSnapshot.getChildren()){
                    kategoriDb.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            for (final DataSnapshot kategoriAd:data.getChildren()){
                                count[0] += (int) data.getChildrenCount();
                                kategoriView = layoutInflater.inflate(R.layout.kategorilisteleme,anasayfaLinearLayout,false);
                                CircleImageView imageViewkategoriItem = kategoriView.findViewById(R.id.imageViewkategoriItem);
                                final TextView textViewKategoriAd = kategoriView.findViewById(R.id.textViewKategoriAd);
                                textViewKategoriAd.setText(kategoriAd.getValue().toString());
                                Glide.with(kategoriView.getContext()).load(icon[count[0]]).into(imageViewkategoriItem);
                                imageViewkategoriItem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        urunIdCekme(data.getKey().toString(),kategoriAd.getValue().toString());
                                    }
                                });
                                anasayfaLinearLayout.addView(kategoriView);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonKategoriTemizle:
                katId=null;
                urunIdCekme(katId,"");
                break;
        }
    }
}
