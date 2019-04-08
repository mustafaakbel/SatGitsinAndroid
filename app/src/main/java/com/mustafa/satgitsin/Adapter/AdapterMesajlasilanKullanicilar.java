package com.mustafa.satgitsin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mustafa.satgitsin.Activitys.MesajActivity;
import com.mustafa.satgitsin.Moduller.Kullanici;
import com.mustafa.satgitsin.Moduller.Mesaj;
import com.mustafa.satgitsin.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterMesajlasilanKullanicilar extends RecyclerView.Adapter<AdapterMesajlasilanKullanicilar.holder> {
    Context context;
    List<Kullanici> kullanicilar;
    DatabaseReference db;
    String sonMesaj;
    String gonderenId;
    public AdapterMesajlasilanKullanicilar(Context context,List<Kullanici> kullanicilar){
        this.context = context;
        this.kullanicilar = kullanicilar;
    }

    @NonNull
    @Override
    public AdapterMesajlasilanKullanicilar.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.kullanici_gorunumu,parent,false);;
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterMesajlasilanKullanicilar.holder holder, final int position) {
        db = FirebaseDatabase.getInstance().getReference();
        db.child("Mesajlar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data:dataSnapshot.getChildren()){
                    Mesaj mesaj = data.getValue(Mesaj.class);
                    if (mesaj.getGonderenKisiId().equals(FirebaseAuth.getInstance().getUid()) && mesaj.getAliciKisiId().equals(kullanicilar.get(position).getUid()) ||
                            mesaj.getGonderenKisiId().equals(kullanicilar.get(position).getUid()) && mesaj.getAliciKisiId().equals(FirebaseAuth.getInstance().getUid())){
                        sonMesaj = mesaj.getMesajIcerik().toString();
                        gonderenId = mesaj.getGonderenKisiId().toString();
                    }

                }
                if (gonderenId.toString().equals(FirebaseAuth.getInstance().getUid())){
                    holder.mesajIletildiImage.setVisibility(View.VISIBLE);
                }else if(gonderenId.toString().equals(kullanicilar.get(position).getUid())){
                    holder.mesajIletildiImage.setVisibility(View.GONE);
                }
                holder.sonMesaj.setText(sonMesaj.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (kullanicilar.get(position).getProfilFoto().equals("bos")){
            Glide.with(context).load(R.drawable.profil_ic_avatar).into(holder.kullaniciGorunumuAvatar);
        }else{
            Glide.with(context).load(kullanicilar.get(position).getProfilFoto()).into(holder.kullaniciGorunumuAvatar);
        }

        holder.kullaniciGorunumuIsim.setText(kullanicilar.get(position).getIsim());

        //
        holder.KullaniciMesajlasma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mesajlasma = new Intent(context, MesajActivity.class);
                mesajlasma.putExtra("kisiId",kullanicilar.get(position).getUid());
                context.startActivity(mesajlasma);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kullanicilar.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        CircleImageView kullaniciGorunumuAvatar;
        ImageView mesajIletildiImage;
        LinearLayout KullaniciMesajlasma;
        TextView kullaniciGorunumuIsim,sonMesaj;
        public holder(@NonNull View itemView) {
            super(itemView);
            kullaniciGorunumuAvatar = itemView.findViewById(R.id.kullaniciGorunumuAvatar);
            kullaniciGorunumuIsim = itemView.findViewById(R.id.kullaniciGorunumuIsim);
            KullaniciMesajlasma = itemView.findViewById(R.id.KullaniciMesajlasma);
            sonMesaj = itemView.findViewById(R.id.sonMesaj);
            mesajIletildiImage = itemView.findViewById(R.id.mesajIletildiImage);
        }
    }
}
