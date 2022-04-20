package com.atula.doanapplication.ui.ad.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.atula.doanapplication.Interface.IChooseSpecialized;
import com.atula.doanapplication.R;
import com.atula.doanapplication.config.DBFirebaseHelper;
import com.atula.doanapplication.model.CDoAn;
import com.atula.doanapplication.model.CUser;
import com.atula.doanapplication.model.GiaoVien;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class UpdateDoanActivity extends AppCompatActivity implements ValueEventListener {
    EditText edt_Title,edt_gv,edt_Mota,edt_moitruong,edt_thoigian;
    Button btn_Create;
    LinearLayout ln_ChuyenNganh,ln_giaovien;
    GiaoVien giaoVien;
    CDoAn cDoAnCurrent ;
    String keyDA = "";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    AutoCompleteTextView au_ngannh;
    ImageView image_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_doan);
        image_back = findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edt_Title = findViewById(R.id.edt_Title);
        edt_gv = findViewById(R.id.input_gvhd);
        edt_Mota = findViewById(R.id.edt_Mota);
        edt_moitruong = findViewById(R.id.edt_moitruong);
        edt_thoigian = findViewById(R.id.edt_thoigian);
        btn_Create = findViewById(R.id.btn_Create);
        btn_Create.setText("Cập nhật");

        au_ngannh = findViewById(R.id.au_ngannh);
        String[] items = {"Công nghệ phần mềm","Hệ thống thông tin","Khoa học phân tích dữ liệu","Mạng máy tính"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(UpdateDoanActivity.this,R.layout.list_item, items);
        au_ngannh.setAdapter(arrayAdapter);

        ln_giaovien = findViewById(R.id.ln_giaovien);
        ln_giaovien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UpdateDoanActivity.this,GiaoVienActivity.class);
                startActivityForResult(intent1,REQUEST_CODE_EXAMPLE);
            }
        });

        btn_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chuyenNganh = au_ngannh.getText().toString();
                String tenDoAn = edt_Title.getText().toString();
                String mucTieu = edt_Mota.getText().toString();
                String moiTruong = edt_moitruong.getText().toString();
                String thoiGian = edt_thoigian.getText().toString();
                if(!TextUtils.isEmpty(chuyenNganh) &&
                        !TextUtils.isEmpty(tenDoAn) &&
                        !TextUtils.isEmpty(mucTieu) &&
                        !TextUtils.isEmpty(moiTruong) &&
                        !TextUtils.isEmpty(thoiGian) && giaoVien != null
                ){
//                    CDoAn cDoAn = new CDoAn(tenDoAn,mucTieu,moiTruong,thoiGian,chuyenNganh,giaoVien);
//                    cDoAn.setKey(key);
                    cDoAnCurrent.setTenDA(tenDoAn);
                    cDoAnCurrent.setThoiGian(thoiGian);
                    cDoAnCurrent.setMoiTruong(moiTruong);
                    cDoAnCurrent.setGiaoVien(giaoVien);
                    cDoAnCurrent.setMucTieuDetai(mucTieu);
                    cDoAnCurrent.setChuyeNganh(chuyenNganh);

                    DBFirebaseHelper db = DBFirebaseHelper.getInstance();
                    db.updateDoAn(cDoAnCurrent);
                    finish();
                }else{

                    Toast.makeText(UpdateDoanActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Intent intent = getIntent();
        String extra = intent.getStringExtra("EXTRA");
        Gson gson = new Gson();
        CDoAn cDoAn = gson.fromJson(extra,CDoAn.class);
        keyDA = cDoAn.getKey();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(DBFirebaseHelper.TABLE_DOAN).child(keyDA);
        databaseReference.addValueEventListener(this);


    }
    

    boolean isFirst = true;
    @Override
    public void onDataChange( DataSnapshot snapshot) {
        if(snapshot.getValue() != null){
            CDoAn cDoAn = snapshot.getValue(CDoAn.class);
            cDoAnCurrent = cDoAn;
            if(isFirst){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        edt_Title.setText(cDoAn.getTenDA());
                        au_ngannh.setText(cDoAn.getChuyeNganh());
                        edt_thoigian.setText(cDoAn.getThoiGian());
                        edt_moitruong.setText(cDoAn.getMoiTruong());
                        edt_Mota.setText(cDoAn.getMucTieuDetai());
                        giaoVien = new GiaoVien(cDoAn.getGiaoVien());
                        edt_gv.setText(giaoVien.showInfo());
                    }
                });
            }



        }
    }

    @Override
    public void onCancelled(DatabaseError error) {

    }



    private static final int REQUEST_CODE_EXAMPLE = 1133;
    public static final String EXTRA_DATA = "EXTRA_DATA";
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_EXAMPLE) {
            if(resultCode == Activity.RESULT_OK) {
                final String result = data.getStringExtra(EXTRA_DATA);
                Gson gson = new Gson();
                GiaoVien value = gson.fromJson(result,GiaoVien.class);
                edt_gv.setText(value.showInfo());
                giaoVien = new GiaoVien(value);
            } else {

            }
        }
    }



}