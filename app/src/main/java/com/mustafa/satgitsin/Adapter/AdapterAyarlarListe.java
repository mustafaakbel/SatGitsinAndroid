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
import android.widget.TextView;

import com.mustafa.satgitsin.Activitys.AyarDegistirme;
import com.mustafa.satgitsin.Moduller.AyarlarItem;
import com.mustafa.satgitsin.R;

import java.util.List;

import static android.media.CamcorderProfile.get;

public class AdapterAyarlarListe extends RecyclerView.Adapter<AdapterAyarlarListe.holder>{

    Context context;
    List<AyarlarItem> liste;

    public AdapterAyarlarListe(Context context , List<AyarlarItem> liste){
        this.liste=liste;
        this.context=context;
    }
    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ayarlar_liste,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, final int position) {

        holder.textViewListeElemani.setText(liste.get(position).getItemAd());
        holder.imageViewSagOk.setImageResource(liste.get(position).getSagOk());
        holder.imageViewAyarlarIcon.setImageResource(liste.get(position).getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tiklanmaolayi",liste.get(position).getItemAd());
                Intent intent = new Intent(context, AyarDegistirme.class);
                intent.putExtra("listeadi",liste.get(position).getItemAd());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }



    public class holder extends RecyclerView.ViewHolder{
        ImageView imageViewAyarlarIcon,imageViewSagOk;
        TextView textViewListeElemani;

        public holder(@NonNull View itemView) {
            super(itemView);
            imageViewSagOk = itemView.findViewById(R.id.imageViewSagOk);
            textViewListeElemani = itemView.findViewById(R.id.textViewListeElemani);
            imageViewAyarlarIcon = itemView.findViewById(R.id.imageViewAyarlarIcon);

        }

    }
}
