package com.mustafa.satgitsin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;
import com.mustafa.satgitsin.Moduller.Urun;
import com.mustafa.satgitsin.R;
import com.mustafa.satgitsin.Activitys.UrunDetay;

import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterUrunListeleme extends RecyclerView.Adapter<AdapterUrunListeleme.holder> {
    Context context;
    List<Urun> urun;
    View view;
    public AdapterUrunListeleme(Context context ,List<Urun> urun){
        this.context=context;
        this.urun=urun;
    }


    @NonNull
    @Override
    public AdapterUrunListeleme.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.urun_liste,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterUrunListeleme.holder holder, final int position) {
        holder.texViewUrunAd.setText(urun.get(position).getUrunBaslik().toString());
        holder.textViewUrunFiyat.setText(String.valueOf(urun.get(position).getUrunFiyat()));
        Glide.with(view.getContext()).load(urun.get(position).getUrunFoto()).into(holder.imageViewUrun);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent urunDetay = new Intent(context, UrunDetay.class);
                urunDetay.putExtra("urun", (Serializable) urun.get(position));
                context.startActivity(urunDetay);
            }
        });

    }


    @Override
    public int getItemCount() {
        return urun.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        CircleImageView imageViewUrun;
        TextView texViewUrunAd,textViewUrunFiyat;
        public holder(@NonNull View itemView) {
            super(itemView);
            imageViewUrun = itemView.findViewById(R.id.imageViewUrun);
            texViewUrunAd = itemView.findViewById(R.id.texViewUrunAd);
            textViewUrunFiyat = itemView.findViewById(R.id.textViewUrunFiyat);
        }
    }
}
