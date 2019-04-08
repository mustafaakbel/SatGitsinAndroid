package com.mustafa.satgitsin.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.mustafa.satgitsin.Moduller.Urun;
import com.mustafa.satgitsin.R;

public class AdapterSlider extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    StorageReference urunDb;
    Urun urun;

    public AdapterSlider(Context context, Urun urun){
        this.context=context;
        this.urun=urun;
    }

    @Override
    public int getCount() {
        return Integer.parseInt(urun.getUrunFotoAdet());
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.urunslider,container,false);
        urunDb = FirebaseStorage.getInstance().getReference("Urunler/").child(urun.getUrunId());
        urunDb.child("item"+position).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageView imageViewurunSlider = view.findViewById(R.id.imageViewurunSlider);
                Glide.with(view).load(uri).into(imageViewurunSlider);
                container.addView(view);
            }
        });
        return view;


    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
