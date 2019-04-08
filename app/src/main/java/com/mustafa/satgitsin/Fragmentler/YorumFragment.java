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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mustafa.satgitsin.Adapter.AdapterUrunListeleme;
import com.mustafa.satgitsin.Adapter.AdapterYorum;
import com.mustafa.satgitsin.Moduller.Yorum;
import com.mustafa.satgitsin.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class YorumFragment extends Fragment implements View.OnClickListener {

    View view;
    String id;
    EditText editTextKullaniciyaYorum;
    Button buttokKullaniciyaYorumYap;
    LinearLayout linearLayoutYorum;
    DatabaseReference db;
    RecyclerView listViewYorumlar;
    RecyclerView.LayoutManager layoutManager;
    List<Yorum> listeYorum;
    AdapterYorum adapterYorum;
    public YorumFragment(String id) {
        this.id=id;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yorum, container, false);
        tanimla();
        yorumCek();
        return view;
    }
    void tanimla(){
        buttokKullaniciyaYorumYap = view.findViewById(R.id.buttokKullaniciyaYorumYap);
        editTextKullaniciyaYorum = view.findViewById(R.id.editTextKullaniciyaYorum);
        linearLayoutYorum= view.findViewById(R.id.linearLayoutYorum);
        listViewYorumlar= view.findViewById(R.id.listViewYorumlar);
        layoutManager= new LinearLayoutManager(getContext());
        listViewYorumlar.setLayoutManager(layoutManager);
        listeYorum = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("Yorumlar");
        db.keepSynced(true);
        if (!id.equals("")){
            linearLayoutYorum.setVisibility(View.VISIBLE);
        }
        buttokKullaniciyaYorumYap.setOnClickListener(this);
    }
    void yorumCek(){
        if (!id.equals("")){
            db.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listeYorum.clear();
                    for (DataSnapshot veriler:dataSnapshot.getChildren()){
                        Yorum cekilenYorum = veriler.getValue(Yorum.class);
                        listeYorum.add(cekilenYorum);
                    }
                    adapterYorum=new AdapterYorum(getContext(),listeYorum);
                    listViewYorumlar.setAdapter(adapterYorum);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else if(id.equals("")){
            db.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listeYorum.clear();
                    for (DataSnapshot veriler:dataSnapshot.getChildren()){
                        Yorum cekilenYorum = veriler.getValue(Yorum.class);
                        listeYorum.add(cekilenYorum);
                    }

                    adapterYorum=new AdapterYorum(getContext(),listeYorum);
                    listViewYorumlar.setAdapter(adapterYorum);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    void yorumEkle(){
        Yorum yorum = new Yorum(editTextKullaniciyaYorum.getText().toString(), FirebaseAuth.getInstance().getUid());
        String randomId = db.push().getKey();
        db.child(id).child(randomId).setValue(yorum);
        editTextKullaniciyaYorum.setText("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttokKullaniciyaYorumYap:
                yorumEkle();
                break;
        }
    }
}
