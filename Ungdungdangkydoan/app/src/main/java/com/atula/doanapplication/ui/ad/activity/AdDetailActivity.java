package com.atula.doanapplication.ui.ad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atula.doanapplication.Interface.ItemClick;
import com.atula.doanapplication.R;
import com.atula.doanapplication.adapter.SVAdapter;
import com.atula.doanapplication.config.DBFirebaseHelper;
import com.atula.doanapplication.config.InfoUser;
import com.atula.doanapplication.model.CDoAn;
import com.atula.doanapplication.model.CUser;
import com.atula.doanapplication.model.GiaoVien;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AdDetailActivity extends AppCompatActivity implements ValueEventListener {
    EditText txt_gv,txt_Mota,txt_moitruong,txt_thoigian,txt_trangthai,txt_chuyenganh,txt_diem;
    TextView txt_Title,txt_Dki;
    LinearLayout ln_sv;
    String keyDA;
    boolean isUser = true;
    CDoAn cDoAnCurrent;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    RecyclerView rcv_dsSV;
    ArrayList<CUser> arrDSSV;
    SVAdapter svAdapter;

    ImageView image_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_detail);
        image_back = findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_Title = findViewById(R.id.txt_Title);
        txt_gv = findViewById(R.id.txt_gv);
        txt_Mota = findViewById(R.id.txt_Mota);
        txt_moitruong = findViewById(R.id.txt_moitruong);
        txt_thoigian = findViewById(R.id.txt_thoigian);
        txt_trangthai = findViewById(R.id.txt_trangthai);
        txt_chuyenganh = findViewById(R.id.txt_chuyenganh);
        txt_diem = findViewById(R.id.txt_diem);
        txt_Dki = findViewById(R.id.txt_Dki);
        ln_sv = findViewById(R.id.ln_sv);
        rcv_dsSV = findViewById(R.id.rcv_dsSV);
        arrDSSV = new ArrayList<>();
        svAdapter = new SVAdapter(AdDetailActivity.this,arrDSSV);
        svAdapter.setItemClick(new ItemClick() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdDetailActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_dsSV.setLayoutManager(linearLayoutManager);

        rcv_dsSV.setAdapter(svAdapter);

        Intent intent = getIntent();
        String value = intent.getStringExtra("EXTRA");
        isUser = intent.getBooleanExtra("ISUSER",true);
        Gson gson = new Gson();
        CDoAn cDoAn = gson.fromJson(value,CDoAn.class);
        keyDA = cDoAn.getKey();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(DBFirebaseHelper.TABLE_DOAN).child(keyDA);
        databaseReference.addValueEventListener(this);

        txt_Dki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!InfoUser.getInstance().getcUser().checkIsNhomTruong()){
                    Toast.makeText(AdDetailActivity.this, "Chỉ có nhóm trưởng được thực hiên thao tác này", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(InfoUser.getInstance().getcUser().getMaDoAn().length() != 0 ){
                    Toast.makeText(AdDetailActivity.this, "Bạn đã đăng kí đồ án rồi !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cDoAnCurrent != null){
                    if(InfoUser.getInstance().getcUser().getMaDoAn().length() == 0){
                        FirebaseDatabase fb = FirebaseDatabase.getInstance();
                        DatabaseReference dt = fb.getReference().child(DBFirebaseHelper.TABLE_GROUP).child(InfoUser.getInstance().getcUser().getMssv());
                        final boolean[] isCallBackFirst = {true};
                        dt.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                if(isCallBackFirst[0] == true){
                                    isCallBackFirst[0] = false;
                                    if(snapshot.getValue() == null){
                                        //chua co nhom
                                        ArrayList<CUser> list = new ArrayList<>();
                                        list.add(InfoUser.getInstance().getcUser());
                                        cDoAnCurrent.setListUser(list);
                                        cDoAnCurrent.setTrangThai(1);
                                        databaseReference.setValue(cDoAnCurrent);

                                        InfoUser.getInstance().getcUser().setMaDoAn(cDoAnCurrent.getKey());
                                        firebaseDatabase.getReference().
                                                child(DBFirebaseHelper.TABLE_USER).
                                                child(InfoUser.getInstance().getcUser().getMssv()).
                                                setValue(InfoUser.getInstance().getcUser());
                                    }else{
                                        //da co nhom
                                        ArrayList<CUser> list = new ArrayList<>();
                                        for (DataSnapshot messageSnapshot: snapshot.getChildren()) {
                                            CUser c = messageSnapshot.getValue(CUser.class);
                                            c.setMaDoAn(cDoAnCurrent.getKey());
                                            list.add(c);
                                        }
                                        cDoAnCurrent.setListUser(list);
                                        cDoAnCurrent.setTrangThai(1);
                                        databaseReference.setValue(cDoAnCurrent);

                                        InfoUser.getInstance().getcUser().setMaDoAn(cDoAnCurrent.getKey());
                                        for(CUser cu : list){
                                            firebaseDatabase.getReference().
                                                    child(DBFirebaseHelper.TABLE_USER).
                                                    child(cu.getMssv()).
                                                    child("maDoAn").
                                                    setValue(cDoAnCurrent.getKey());
                                        }

                                        firebaseDatabase.getReference()
                                                .child(DBFirebaseHelper.TABLE_GROUP)
                                                .child(InfoUser.getInstance().getcUser().getMssv())
                                                .setValue(list);

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull  DatabaseError error) {

                            }
                        });

                    }



                }
            }
        });
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        if(snapshot.getValue() != null){
            CDoAn cDoAn = snapshot.getValue(CDoAn.class);
            cDoAnCurrent = cDoAn;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txt_Title.setText(cDoAn.getTenDA());
                    txt_chuyenganh.setText(cDoAn.getChuyeNganh());
                    txt_thoigian.setText(cDoAn.getThoiGian());
                    txt_moitruong.setText(cDoAn.getMoiTruong());
                    txt_Mota.setText(cDoAn.getMucTieuDetai());
                    txt_trangthai.setText(cDoAn.getTrangThaiString());
                    txt_gv.setText(cDoAn.getGiaoVien().showInfo());
                    txt_diem.setText(cDoAn.getDiem());

                    if(!isUser){
                        txt_Dki.setVisibility(View.GONE);
                    }else{
                        if(cDoAn.getTrangThai() == 1 ){
                            txt_Dki.setVisibility(View.GONE);
                        }else {
                            txt_Dki.setVisibility(View.VISIBLE);
                        }
                    }
                    if(cDoAn.getTrangThai() == 0){
                        ln_sv.setVisibility(View.GONE);
                    }else {
                        //
                        arrDSSV.clear();
                        ln_sv.setVisibility(View.VISIBLE);

                        for(int i = 0 ; i < cDoAn.getListUser().size() ; i++){
                            CUser us = cDoAn.getListUser().get(i);
                            arrDSSV.add(us);
                        }
                        svAdapter.notifyDataSetChanged();
                    }
                }
            });

        }

    }

    @Override
    public void onCancelled(DatabaseError error) {

    }
}