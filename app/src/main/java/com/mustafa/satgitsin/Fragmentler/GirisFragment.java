package com.mustafa.satgitsin.Fragmentler;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mustafa.satgitsin.Activitys.MainActivity;
import com.mustafa.satgitsin.R;
import com.mustafa.satgitsin.Activitys.SifremiUnuttum;


/**
 * A simple {@link Fragment} subclass.
 */
public class GirisFragment extends Fragment implements View.OnClickListener{
    private FirebaseAuth auth;
    private EditText editTextGirisMail,editTextGirisSifre;
    private TextView textViewSifremiUnuttum;
    private Button buttonFragmentGiris;
    private View view;
    private ProgressDialog progressDialog;
    DatabaseReference db;
    public GirisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_giris,container,false);
        tanimla();
        return view;
    }

    void tanimla(){
        editTextGirisMail = view.findViewById(R.id.editTextGirisMail);
        editTextGirisSifre = view.findViewById(R.id.editTextGirisSifre);
        textViewSifremiUnuttum = view.findViewById(R.id.textViewSifremiUnuttum);
        buttonFragmentGiris = view.findViewById(R.id.buttonFragmentGiris);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Giriş");
        progressDialog.setMessage("Giriş yapılıyor lütfen bekleyiniz..");
        progressDialog.setCanceledOnTouchOutside(false);
        db = FirebaseDatabase.getInstance().getReference();
        db.keepSynced(true);
        auth = FirebaseAuth.getInstance();
        buttonFragmentGiris.setOnClickListener(this);
        textViewSifremiUnuttum.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()){
            case R.id.buttonFragmentGiris:
                progressDialog.show();
                auth.signInWithEmailAndPassword(editTextGirisMail.getText().toString(),editTextGirisSifre.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            String token = FirebaseInstanceId.getInstance().getToken();
                            Log.d("asdasdasd",token);
                            db.child("Kullanici").child(FirebaseAuth.getInstance().getUid()).child("Token").setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent anasayfa = new Intent(getActivity(),MainActivity.class);
                                    anasayfa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(anasayfa);
                                    getActivity().finish();
                                }
                            });


                        }else{
                            progressDialog.hide();
                            Toast.makeText(getContext(),"Hatalı mail veya şifre..",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.textViewSifremiUnuttum:
                Intent sifremiunuttum = new Intent(getActivity(),SifremiUnuttum.class);
                startActivity(sifremiunuttum);
                break;
        }
    }

}
