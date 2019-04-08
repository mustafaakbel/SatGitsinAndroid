package com.mustafa.satgitsin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mustafa.satgitsin.Activitys.MainActivity;
import com.mustafa.satgitsin.Activitys.ProfilActivity;
import com.mustafa.satgitsin.Moduller.Kullanici;
import com.mustafa.satgitsin.Moduller.Yorum;
import com.mustafa.satgitsin.R;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterYorum extends RecyclerView.Adapter<AdapterYorum.holder> {
    Context context;
    List<Yorum> yorumlar;
    View view;
    DatabaseReference Kullanicidb;
    public AdapterYorum(Context context,List<Yorum> yorumlar){
        this.context=context;
        this.yorumlar=yorumlar;
        Collections.reverse(yorumlar);
    }

    @NonNull
    @Override
    public AdapterYorum.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.yorumgorunumu,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterYorum.holder holder, final int position) {
        Kullanicidb = FirebaseDatabase.getInstance().getReference("Kullanici");
        Kullanicidb.keepSynced(true);
        Kullanicidb.child(yorumlar.get(position).getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    final Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                holder.textViewYorumYapan.setText(kullanici.getIsim().toString());
                holder.textViewYorumGoster.setText(yorumlar.get(position).getIcerikYorum());
                if (kullanici.getProfilFoto().equals("bos")){
                    Glide.with(view).load(R.drawable.profil_ic_avatar).into(holder.yorumImageview);
                }else{
                    Glide.with(view).load(kullanici.getProfilFoto()).into(holder.yorumImageview);
                }

                holder.textViewYorumYapan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (kullanici.getUid().equals(FirebaseAuth.getInstance().getUid())){
                            Intent profil = new Intent(context, MainActivity.class);
                            profil.putExtra("fragmentItem","3");
                            context.startActivity(profil);
                        }else{
                            Intent profil = new Intent(context, ProfilActivity.class);
                            profil.putExtra("profilId",kullanici.getUid());
                            context.startActivity(profil);
                        }


                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return yorumlar.size();
    }
    public class holder extends RecyclerView.ViewHolder{
        CircleImageView yorumImageview;
        TextView textViewYorumYapan,textViewYorumGoster;
        public holder(@NonNull View itemView) {
            super(itemView);
            textViewYorumYapan=itemView.findViewById(R.id.textViewYorumYapan);
            textViewYorumGoster=itemView.findViewById(R.id.textViewYorumGoster);
            yorumImageview=itemView.findViewById(R.id.yorumImageview);
        }
    }
}
