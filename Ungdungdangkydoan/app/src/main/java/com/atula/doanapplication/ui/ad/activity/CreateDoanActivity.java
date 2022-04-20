package com.atula.doanapplication.ui.ad.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atula.doanapplication.Interface.IChooseSpecialized;
import com.atula.doanapplication.R;
import com.atula.doanapplication.config.DBFirebaseHelper;
import com.atula.doanapplication.model.CDoAn;
import com.atula.doanapplication.model.GiaoVien;
import com.atula.doanapplication.ui.user.activity.UpdateUserActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class CreateDoanActivity extends AppCompatActivity {
    EditText edt_Title,edt_gv,edt_Mota,edt_moitruong,edt_thoigian;
    Button btn_Create;
    LinearLayout ln_ChuyenNganh,ln_giaovien;
    GiaoVien giaoVien;

    AutoCompleteTextView au_ngannh;
    ImageView image_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_doan);
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

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        edt_Title.setText(title);

        au_ngannh = findViewById(R.id.au_ngannh);
        String[] items = {"Công nghệ phần mềm","Hệ thống thông tin","An toàn thông tin","Mạng máy tính"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(CreateDoanActivity.this,R.layout.list_item, items);
        au_ngannh.setAdapter(arrayAdapter);


        ln_giaovien = findViewById(R.id.ln_giaovien);
        ln_giaovien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CreateDoanActivity.this,GiaoVienActivity.class);
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
                    CDoAn cDoAn = new CDoAn(tenDoAn,mucTieu,moiTruong,thoiGian,chuyenNganh,giaoVien);
                    DBFirebaseHelper db = DBFirebaseHelper.getInstance();
                    final boolean[] isCallBackFirst = {true};
                    db.createDoAn(cDoAn,new ValueEventListener() {
                        @Override
                        public void onDataChange( DataSnapshot snapshot) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(isCallBackFirst[0]) {
                                        isCallBackFirst[0] = false;
                                        Toast.makeText(CreateDoanActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });

                }else{
                    Toast.makeText(CreateDoanActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }

            }
        });
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