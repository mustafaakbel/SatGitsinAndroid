package com.mustafa.satgitsin.Fragmentler;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.mustafa.satgitsin.Activitys.KullanicAyarlari;
import com.mustafa.satgitsin.Activitys.MainActivity;
import com.mustafa.satgitsin.Moduller.Urun;
import com.mustafa.satgitsin.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class IlanverFragment extends Fragment  implements View.OnClickListener{

    Button yukle,buttonIlanVer;
    View view,galeriView;
    Uri yol;
    LinearLayout galeri;
    LayoutInflater layoutInflater;
    Spinner spinnerKategori;
    List<String> kategoriler;
    ArrayAdapter<String> kategorilerAdapter;
    HorizontalScrollView scrollView;
    DatabaseReference db;
    List<Uri> fotograflar;
    EditText editTextUrunOzellikleri,ediTextUrunFiyat,ediTextUrunBaslik;
    DatabaseReference urunDb;
    StorageReference urunStorage;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    int boyut=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ilanver, container, false);
        layoutInflater = inflater;
        tanimla();
        kategoriDoldur();

        return view;
    }



    void tanimla(){
        yukle = view.findViewById(R.id.yukle);
        galeri = view.findViewById(R.id.galeri);
        spinnerKategori = view.findViewById(R.id.spinnerKategori);
        buttonIlanVer = view.findViewById(R.id.buttonIlanVer);
        kategoriler = new ArrayList<String>();
        scrollView = view.findViewById(R.id.scrollView);
        fotograflar = new ArrayList<>();
        editTextUrunOzellikleri = view.findViewById(R.id.editTextUrunOzellikleri);
        urunDb = FirebaseDatabase.getInstance().getReference("Urunler");
        urunDb.keepSynced(true);
        ediTextUrunFiyat = view.findViewById(R.id.ediTextUrunFiyat);
        ediTextUrunBaslik=view.findViewById(R.id.ediTextUrunBaslik);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("Kategoriler");
        db.keepSynced(true);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("İlan Yayınlanıyor");
        progressDialog.setMessage("İlan yayınlanıyor lütfen bekleyiniz");
        progressDialog.setCanceledOnTouchOutside(false);
        yukle.setOnClickListener(this);
        buttonIlanVer.setOnClickListener(this);
    }

    //Galeriden foto çekme
    void galeridenFotoSec(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(Intent.createChooser(intent,"Resim seçiniz"),1);
    }
    void kategoriDoldur(){
/*      //Kategori doldurma işlemi
        String[] dizi = {"Araba","Emlak","Hizmetler","Elektronik","Ev ve Bahçe","Spor, Eğlence ve Oyunlar","Diğer Araçlar ve Yedek parçalar","Giyim ve Aksesuar","Bebek ve Çocuk","Film,Kitap ve Müzik","Diğer"};
        for (int i = 0;i<dizi.length;i++){
            String id = db.push().getKey();

            db.child(id).child("kategoriAd").setValue(dizi[i]);
        }*/
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String id = postSnapshot.child("kategoriAd").getValue().toString();
                    kategoriler.add(id.toString());

                }
                kategorilerAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, kategoriler);
                kategorilerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerKategori.setAdapter(kategorilerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getClipData() != null){
                boyut = data.getClipData().getItemCount();
            }else if(data.getData() != null){
                boyut = 1;
            }

            if (boyut<=5 && requestCode==1){
                if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getClipData() != null){
                    scrollView.setMinimumHeight(190);
                    galeri.removeAllViews();
                    fotograflar.clear();
                    //Çoklu foto seçtiğinde
                    for (int i = 0; i< boyut ; i++){
                        galeriView = layoutInflater.inflate(R.layout.galeri_item,galeri,false);
                        yol = data.getClipData().getItemAt(i).getUri();
                        ImageView galeriFoto = galeriView.findViewById(R.id.galeriFoto);
                        TextView galeriFotoIsım = galeriView.findViewById(R.id.galeriFotoIsım);
                        galeriFotoIsım.setText("");
                        galeriFoto.setImageURI(yol);
                        fotograflar.add(yol);
                        galeri.addView(galeriView);
                    }
                    //Tek foto seçtiğinde
                }else if(data.getData() != null){
                    scrollView.setMinimumHeight(190);
                    galeri.removeAllViews();
                    fotograflar.clear();
                    yol = data.getData();
                    galeriView = layoutInflater.inflate(R.layout.galeri_item,galeri,false);
                    ImageView galeriFoto = galeriView.findViewById(R.id.galeriFoto);
                    TextView galeriFotoIsım = galeriView.findViewById(R.id.galeriFotoIsım);
                    galeriFotoIsım.setText("item0");
                    galeriFoto.setImageURI(yol);
                    fotograflar.add(yol);
                    galeri.addView(galeriView);
                }
            }else if(boyut>5){
                Toast.makeText(getContext(), "En fazla 5 fotoğraf yükleyebilirsiniz..", Toast.LENGTH_SHORT).show();
            }
        }
    }
    final boolean[] kontrol = {true};
    void veriAlKaydet(){
        if (boyut!=0){
            if (!TextUtils.isEmpty(editTextUrunOzellikleri.getText().toString()) && !TextUtils.isEmpty(ediTextUrunFiyat.getText().toString())
                    && !TextUtils.isEmpty(ediTextUrunBaslik.getText().toString())){
                progressDialog.show();
                final String urunId = urunDb.push().getKey();
                urunStorage = FirebaseStorage.getInstance().getReference("Urunler/").child(urunId);

                for (int i = 0; i< fotograflar.size();i++)
                {
                    urunStorage.child("item"+i).putFile(fotograflar.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                if (kontrol[0]==true) {
                                    urunStorage.child("item0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(final Uri uri) {
                                            final Uri fotoYol = uri;
                                            final String kategoriAdi = spinnerKategori.getSelectedItem().toString();
                                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot veriler: dataSnapshot.getChildren()) {
                                                        if (kategoriAdi.equals(veriler.child("kategoriAd").getValue().toString())){
                                                            final String kategoriId = veriler.getKey();
                                                            String urunGenelOzellikleri = editTextUrunOzellikleri.getText().toString();
                                                            int urunFiyat = Integer.parseInt(ediTextUrunFiyat.getText().toString());
                                                            String urunBaslik = ediTextUrunBaslik.getText().toString();
                                                            String uid = auth.getCurrentUser().getUid();
                                                            Urun urun = new Urun(urunId,kategoriId,urunBaslik,urunGenelOzellikleri,urunFiyat,uid,uri.toString(),String.valueOf(fotograflar.size()));
                                                            urunDb.child(urunId).setValue(urun);
                                                            progressDialog.dismiss();
                                                            Intent anasayfa = new Intent(getContext(),MainActivity.class);
                                                            startActivity( anasayfa);
                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    });
                                    kontrol[0] =false;
                                }

                            }
                        }
                    });
                }

            }else{
                Toast.makeText(getContext(), "Tüm alanlar doldurulmalıdır.", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(getContext(), "En az bir fotoğraf yüklemeniz gerekmektedir.", Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    public void onStop() {
        super.onStop();
        boyut=0;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.yukle:
                galeridenFotoSec();
                break;
            case R.id.buttonIlanVer:
                veriAlKaydet();
                break;
        }
    }
}
