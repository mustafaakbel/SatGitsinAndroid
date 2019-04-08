package com.mustafa.satgitsin.Activitys;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.mustafa.satgitsin.Moduller.Urun;
import com.mustafa.satgitsin.R;

import java.util.ArrayList;
import java.util.List;

public class IlanDuzenleActivity extends AppCompatActivity implements View.OnClickListener{

    Urun urun;
    DatabaseReference db,Urundb;
    List<String> kategoriler;
    ArrayAdapter<String> kategorilerAdapter;
    EditText urunBaslikGuncelle,urunGenelOzellikleriGuncelle,urunFiyatGuncelle;
    Button fotoGuncelle,buttonIlanGuncelle;
    LinearLayout fotoGuncelleLinearLayout;
    Spinner spinnerKategoriGuncelle;
    StorageReference urunfotoDb;
    HorizontalScrollView scrollViewFotoGuncelle;
    View sliderView;
    List<Uri> fotograflar;
    int boyut=0;
    final boolean[] kontrol = {true};
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilan_duzenle);
        tanimla();
    }

    void tanimla(){
        urun =(Urun) getIntent().getSerializableExtra("urun");
        db = FirebaseDatabase.getInstance().getReference("Kategoriler");
        Urundb = FirebaseDatabase.getInstance().getReference("Urunler");
        urunfotoDb = FirebaseStorage.getInstance().getReference("Urunler");
        db.keepSynced(true);
        Urundb.keepSynced(true);
        urunBaslikGuncelle = findViewById(R.id.urunBaslikGuncelle);
        urunGenelOzellikleriGuncelle = findViewById(R.id.urunGenelOzellikleriGuncelle);
        urunFiyatGuncelle = findViewById(R.id.urunFiyatGuncelle);
        fotoGuncelle = findViewById(R.id.fotoGuncelle);
        buttonIlanGuncelle = findViewById(R.id.buttonIlanGuncelle);
        fotoGuncelleLinearLayout = findViewById(R.id.fotoGuncelleLinearLayout);
        spinnerKategoriGuncelle = findViewById(R.id.spinnerKategoriGuncelle);
        kategoriler = new ArrayList<String>();
        fotograflar = new ArrayList<>();
        scrollViewFotoGuncelle = findViewById(R.id.scrollViewFotoGuncelle);
        progressDialog =  new ProgressDialog(this);
        progressDialog.setTitle("Ürün güncelleniyor");
        progressDialog.setMessage("Ürününüz güncellenmektedir lütfen bekleyiniz..");
        progressDialog.setCanceledOnTouchOutside(false);
        fotoGuncelle.setOnClickListener(this);
        buttonIlanGuncelle.setOnClickListener(this);
        FotolariGetir();
        kategoriDoldur();
        verileriDoldurma();
    }

    void kategoriDoldur(){
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int secili_kategori_indis=0;
                int i=0;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String id = postSnapshot.child("kategoriAd").getValue().toString();
                    kategoriler.add(id.toString());
                    if (postSnapshot.getKey().equals(urun.getKategoriAdi())){
                        secili_kategori_indis=i;
                    }else{
                        i++;
                    }
                }
                kategorilerAdapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_item, kategoriler);
                kategorilerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerKategoriGuncelle.setAdapter(kategorilerAdapter);
                spinnerKategoriGuncelle.setSelection(secili_kategori_indis);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    void verileriDoldurma(){
        urunBaslikGuncelle.setText(urun.getUrunBaslik().toString());
        urunGenelOzellikleriGuncelle.setText(urun.getUrunOzellikleri().toString());
        urunFiyatGuncelle.setText(String.valueOf(urun.getUrunFiyat()));
    }

    void galeridenFotoSec(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(Intent.createChooser(intent,"Resim seçiniz"),1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Uri yol;
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getClipData() != null){
                boyut = data.getClipData().getItemCount();
            }else if(data.getData() != null){
                boyut = 1;
            }

            if (boyut<=5 && requestCode==1){
                if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getClipData() != null){
                    sliderView.setMinimumHeight(190);
                    fotoGuncelleLinearLayout.removeAllViews();
                    fotograflar.clear();
                    //Çoklu foto seçtiğinde
                    for (int i = 0; i< boyut ; i++){
                        sliderView =LayoutInflater.from(getBaseContext()).inflate(R.layout.galeri_item,fotoGuncelleLinearLayout,false);
                        yol = data.getClipData().getItemAt(i).getUri();
                        ImageView galeriFoto = sliderView.findViewById(R.id.galeriFoto);
                        TextView galeriFotoIsım = sliderView.findViewById(R.id.galeriFotoIsım);
                        galeriFotoIsım.setText("");
                        galeriFoto.setImageURI(yol);
                        fotograflar.add(yol);
                        fotoGuncelleLinearLayout.addView(sliderView);
                    }
                    //Tek foto seçtiğinde
                }else if(data.getData() != null){
                    sliderView.setMinimumHeight(190);
                    fotoGuncelleLinearLayout.removeAllViews();
                    fotograflar.clear();
                    yol = data.getData();
                    sliderView = LayoutInflater.from(getBaseContext()).inflate(R.layout.galeri_item,fotoGuncelleLinearLayout,false);
                    ImageView galeriFoto = sliderView.findViewById(R.id.galeriFoto);
                    TextView galeriFotoIsım = sliderView.findViewById(R.id.galeriFotoIsım);
                    galeriFotoIsım.setText("item0");
                    galeriFoto.setImageURI(yol);
                    fotograflar.add(yol);
                    fotoGuncelleLinearLayout.addView(sliderView);
                }
            }else if(boyut>5){
                Toast.makeText(getBaseContext(), "En fazla 5 fotoğraf yükleyebilirsiniz..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void FotolariGetir(){
        scrollViewFotoGuncelle.setMinimumHeight(190);
        for (int i=0;i<Integer.parseInt(urun.getUrunFotoAdet());i++) {

            //final int finalI = i;
            urunfotoDb.child(urun.getUrunId()).child("item"+i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    sliderView = LayoutInflater.from(getBaseContext()).inflate(R.layout.galeri_item,fotoGuncelleLinearLayout,false);
                    ImageView sliderFoto = sliderView.findViewById(R.id.galeriFoto);
                    Glide.with(IlanDuzenleActivity.this).load(uri).into(sliderFoto);
                    fotoGuncelleLinearLayout.addView(sliderView);
                }
            });
        }
    }

    void verileriGuncelle(){
        progressDialog.show();
        for (int i=0;i<Integer.parseInt(urun.getUrunFotoAdet());i++) {
            urunfotoDb.child("Urunler/").child(urun.getUrunId()).child("item"+i).delete();
        }

        if (boyut!=0){
            if (!TextUtils.isEmpty(urunGenelOzellikleriGuncelle.getText().toString()) && !TextUtils.isEmpty(urunFiyatGuncelle.getText().toString())
                    && !TextUtils.isEmpty(urunBaslikGuncelle.getText().toString())){
                urunfotoDb = FirebaseStorage.getInstance().getReference("Urunler/").child(urun.getUrunId());

                for (int i = 0; i< fotograflar.size();i++)
                {
                    urunfotoDb.child("item"+i).putFile(fotograflar.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                if (kontrol[0]==true) {
                                    urunfotoDb.child("item0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(final Uri uri) {
                                            final Uri fotoYol = uri;
                                            final String kategoriAdi = spinnerKategoriGuncelle.getSelectedItem().toString();
                                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot veriler: dataSnapshot.getChildren()) {
                                                        if (kategoriAdi.equals(veriler.child("kategoriAd").getValue().toString())){
                                                            final String kategoriId = veriler.getKey();
                                                            String urunGenelOzellikleri = urunGenelOzellikleriGuncelle.getText().toString();
                                                            int urunFiyat = Integer.parseInt(urunFiyatGuncelle.getText().toString());
                                                            String urunBaslik = urunBaslikGuncelle.getText().toString();
                                                            String uid = FirebaseAuth.getInstance().getUid();
                                                            Urun YeniUrun = new Urun(urun.getUrunId(),kategoriId,urunBaslik,urunGenelOzellikleri,urunFiyat,uid,fotoYol.toString(),String.valueOf(fotograflar.size()));
                                                            Urundb.child(urun.getUrunId()).setValue(YeniUrun);
                                                            progressDialog.dismiss();
                                                            Intent anasayfa = new Intent(getBaseContext(),MainActivity.class);
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
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(), "Tüm alanlar doldurulmalıdır.", Toast.LENGTH_SHORT).show();
            }

        }else {
            progressDialog.dismiss();
            Toast.makeText(getBaseContext(), "En az bir fotoğraf yüklemeniz gerekmektedir.", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fotoGuncelle:
                galeridenFotoSec();
                break;
            case R.id.buttonIlanGuncelle:
                verileriGuncelle();
                break;
        }
    }
}
