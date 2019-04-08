package com.mustafa.satgitsin.Fragmentler;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mustafa.satgitsin.Moduller.Kullanici;
import com.mustafa.satgitsin.Activitys.MainActivity;
import com.mustafa.satgitsin.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class KayitFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth auth;
    View view;
    EditText editTextKayitFragmentMail,editTextKayitFragmentSifre,editTextKayitFragmentIsim;
    Button buttonFragmentKayit;
    ProgressDialog progressDialog;
    DatabaseReference db;
    public KayitFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_kayit,container, false);
        tanimla();
        return view;
    }

    void tanimla(){
        editTextKayitFragmentMail = view.findViewById(R.id.editTextKayitFragmentMail);
        editTextKayitFragmentSifre = view.findViewById(R.id.editTextKayitFragmentSifre);
        editTextKayitFragmentIsim = view.findViewById(R.id.editTextKayitFragmentIsim);
        buttonFragmentKayit = view.findViewById(R.id.buttonFragmentKayit);
        db = FirebaseDatabase.getInstance().getReference("Kullanici");
        db.keepSynced(true);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Kayıt");
        progressDialog.setMessage("Kaydınız oluşturuluyor lütfen bekleyiniz...");
        progressDialog.setCanceledOnTouchOutside(false);
        auth = FirebaseAuth.getInstance();
        buttonFragmentKayit.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        progressDialog.show();
        auth.createUserWithEmailAndPassword(editTextKayitFragmentMail.getText().toString(),editTextKayitFragmentSifre.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    String uid = auth.getCurrentUser().getUid();
                    String token = FirebaseInstanceId.getInstance().getToken();
                    Kullanici kullanici = new Kullanici(uid.toString(),editTextKayitFragmentMail.getText().toString(),editTextKayitFragmentSifre.getText().toString(),editTextKayitFragmentIsim.getText().toString(),"bos",token);
                    db.child(uid).setValue(kullanici);
                    Intent anasayfa = new Intent(getActivity(),MainActivity.class);
                    anasayfa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(anasayfa);
                    getActivity().finish();
                }

                else{
                    progressDialog.hide();
                    Toast.makeText(getContext(),"Hata oluştu..",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
