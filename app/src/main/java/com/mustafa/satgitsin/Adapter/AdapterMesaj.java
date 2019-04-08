package com.mustafa.satgitsin.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.mustafa.satgitsin.Moduller.Mesaj;
import com.mustafa.satgitsin.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterMesaj extends RecyclerView.Adapter<AdapterMesaj.ViewHolder> {
    public static final int  mesaj_gonderen=1;
    public static final int mesaj_alan=0;
    Context context;
    List<Mesaj> mesajlar;
    String profilFoto;
    public AdapterMesaj(Context context, List<Mesaj> mesajlar,String profilFoto){
        this.context=context;
        this.mesajlar=mesajlar;
        this.profilFoto=profilFoto;
    }
    @NonNull
    @Override
    public AdapterMesaj.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == mesaj_gonderen){
            View view = LayoutInflater.from(context).inflate(R.layout.mesaji_gonderen_kisi,parent,false);
            return new AdapterMesaj.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.mesaji_alan_kisi,parent,false);
            return new AdapterMesaj.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMesaj.ViewHolder holder, int position) {
        Mesaj mesaj = mesajlar.get(position);

        holder.mesajGoster.setText(mesaj.getMesajIcerik().toString());

        if (profilFoto.equals("bos")){
            holder.mesajprofilfoto.setImageResource(R.drawable.profil_ic_avatar);
        }else{
            Glide.with(context).load(profilFoto).into(holder.mesajprofilfoto);
        }


    }

    @Override
    public int getItemCount() {
        return mesajlar.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mesajGoster;
        CircleImageView mesajprofilfoto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mesajGoster = itemView.findViewById(R.id.mesajGoster);
            mesajprofilfoto = itemView.findViewById(R.id.mesajprofilfoto);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mesajlar.get(position).getGonderenKisiId().equals(FirebaseAuth.getInstance().getUid())){
            return mesaj_gonderen;
        }else {
            return mesaj_alan;
        }
    }
}
