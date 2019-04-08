package com.mustafa.satgitsin.Fragmentler;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mustafa.satgitsin.Adapter.AdapterProfilFragment;
import com.mustafa.satgitsin.Moduller.Kullanici;
import com.mustafa.satgitsin.R;

import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment implements View.OnClickListener {
    ProgressDialog progressDialog;
    Button buttonEditImage;
    TextView ProfilIsim,ProfilKonum;
    FirebaseAuth auth;
    View view;
    CircleImageView avatar;
    private StorageReference mStorageRef;
    DatabaseReference db;
    String url;
    Uri yol;
    private final int REQUEST_CODE=71;
    ViewPager viewPagerProfil;
    TabLayout tabLayoutProfil;
    AdapterProfilFragment adapterProfilFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profil, container, false);
        tanimla();
        KullaniciBul();
        FragmentAyari();
        return view;
    }


    void tanimla(){
        avatar = view.findViewById(R.id.avatar);
        ProfilIsim = view.findViewById(R.id.ProfilIsim);
        ProfilKonum = view.findViewById(R.id.ProfilKonum);
        buttonEditImage = view.findViewById(R.id.buttonEditImage);
        viewPagerProfil = view.findViewById(R.id.viewPagerProfil);
        tabLayoutProfil = view.findViewById(R.id.tabLayoutProfil);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Fotoğraf Yükleniyor");
        progressDialog.setMessage("Fotoğraf yükleniyor lütfen bekleyiniz..");
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("Kullanici");
        db.keepSynced(true);
        buttonEditImage.setOnClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    void FragmentAyari(){
        adapterProfilFragment = new AdapterProfilFragment(getChildFragmentManager(),"");
        viewPagerProfil.setAdapter(adapterProfilFragment);
        tabLayoutProfil.setupWithViewPager(viewPagerProfil);
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    void KullaniciBul(){
        db.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                ProfilIsim.setText(kullanici.getIsim());
                ProfilKonum.setText(kullanici.getMail());
                if (kullanici.getProfilFoto().equals("bos")){
                    Glide.with(ProfilFragment.this).load(R.drawable.profil_ic_avatar).into(avatar);
                    buttonEditImage.setVisibility(View.VISIBLE);
                }else{
                    Glide.with(ProfilFragment.this).load(kullanici.getProfilFoto().toString()).into(avatar);
                    buttonEditImage.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    void resimSec(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Fotoğraf seç"),REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            yol = data.getData();
            resimYukle(yol);
            buttonEditImage.setVisibility(View.VISIBLE);
        }
    }
    void resimYukle(final Uri yol){
        progressDialog.show();
        if (yol != null ){

            mStorageRef.child("profil_foto/"+auth.getUid()).putFile(yol).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mStorageRef.child("profil_foto").child(auth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            Glide.with(ProfilFragment.this).load(yol.toString()).into(avatar);
                            db.child(auth.getUid()).child("profilFoto").setValue(uri.toString());
                        }
                    });

                }
            });

            buttonEditImage.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonEditImage:
                resimSec();
                break;
        }

    }

}
