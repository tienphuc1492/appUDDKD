package com.atula.doanapplication.ui.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.atula.doanapplication.R;
import com.atula.doanapplication.config.DBFirebaseHelper;
import com.atula.doanapplication.model.CUser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class XoaThanhVienActivity extends AppCompatActivity implements ValueEventListener {
    String mssv = "";
    CUser cUserCurrent;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextInputEditText input_MSSV,input_Ten,input_NgaySinh,input_ChuyenNganh,input_SDT;
    Button btn_Dki,btn_Exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_thanh_vien);
        input_MSSV = findViewById(R.id.input_MSSV);
        input_MSSV.setEnabled(false);
        input_Ten = findViewById(R.id.input_Ten);
        input_Ten.setEnabled(false);
        input_NgaySinh = findViewById(R.id.input_NgaySinh);
        input_NgaySinh.setEnabled(false);
        input_ChuyenNganh = findViewById(R.id.input_ChuyenNganh);
        input_ChuyenNganh.setEnabled(false);
        input_SDT = findViewById(R.id.input_SDT);
        input_SDT.setEnabled(false);

        ln_NoInfo = findViewById(R.id.ln_NoInfo);
        ln_Info = findViewById(R.id.ln_Info);
        btn_Exit = findViewById(R.id.btn_Exit);
        btn_Dki = findViewById(R.id.btn_Dki);
        btn_Dki.setText("Xoá khỏi nhóm");
        btn_Dki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cUserAdd.setMaDoAn("");
                cUserAdd.setNhomTruong("");

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child(DBFirebaseHelper.TABLE_GROUP).child(cUserCurrent.getMssv());
                final boolean[] isCallBack = {false};
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(isCallBack[0] == false){
                            isCallBack[0] = true;
                            ArrayList<CUser> arrayListt = new ArrayList<>();
                            for (DataSnapshot messageSnapshot: snapshot.getChildren()) {
                                CUser doAn = messageSnapshot.getValue(CUser.class);
                                arrayListt.add(doAn);
                            }
                            int index = -1;
                            for(int i = 0; i < arrayListt.size() ; i++){
                                if(arrayListt.get(i).getMssv().equals(cUserAdd.getMssv())){
                                    index = i;
                                    break;
                                }
                            }
                            arrayListt.remove(index);
                            if(arrayListt.size() == 1){
                                firebaseDatabase.getReference()
                                        .child(DBFirebaseHelper.TABLE_USER)
                                        .child(cUserCurrent.getMssv()).child("nhomTruong").setValue("");
                                firebaseDatabase.getReference()
                                        .child(DBFirebaseHelper.TABLE_GROUP)
                                        .child(cUserCurrent.getMssv()).setValue(null);
                            }else{
                                firebaseDatabase.getReference()
                                        .child(DBFirebaseHelper.TABLE_GROUP)
                                        .child(cUserCurrent.getMssv()).setValue(arrayListt);
                            }

                            firebaseDatabase.getReference()
                                    .child(DBFirebaseHelper.TABLE_USER)
                                    .child(cUserAdd.getMssv()).setValue(cUserAdd);


                            if(cUserCurrent.getMaDoAn().length() != 0 ){
                                firebaseDatabase.getReference()
                                        .child(DBFirebaseHelper.TABLE_DOAN)
                                        .child(cUserCurrent.getMaDoAn())
                                        .child("listUser")
                                        .setValue(arrayListt);

                            }

                            Toast.makeText(XoaThanhVienActivity.this, "Xoá hoàn thành", Toast.LENGTH_SHORT).show();
                            finish();


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });

            }
        });


        Intent intent = getIntent();
        mssv =  intent.getStringExtra("MSSV");
        String a = intent.getStringExtra("EXTRA");
        Gson gson = new Gson();
        cUserCurrent = gson.fromJson(a,CUser.class);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(DBFirebaseHelper.TABLE_USER).child(mssv);
        databaseReference.addValueEventListener(this);

    }

    CUser cUserAdd;
    LinearLayout ln_NoInfo,ln_Info;
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.getValue() == null){
            // khong co user;
            ln_Info.setVisibility(View.GONE);
            ln_NoInfo.setVisibility(View.VISIBLE);
        }else{
            ln_Info.setVisibility(View.VISIBLE);
            ln_NoInfo.setVisibility(View.GONE);
            CUser cUser = snapshot.getValue(CUser.class);
            cUserAdd = cUser;
            input_Ten.setText(cUserAdd.getTen());
            input_ChuyenNganh.setText(cUserAdd.getChuyenNganh());
            input_MSSV.setText(cUserAdd.getMssv());
            input_NgaySinh.setText(cUserAdd.getNgaySinh());
            input_SDT.setText(cUserAdd.getsDT());
            databaseReference.removeEventListener(this);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}