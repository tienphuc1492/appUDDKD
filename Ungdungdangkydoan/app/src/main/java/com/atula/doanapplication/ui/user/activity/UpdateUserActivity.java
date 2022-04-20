package com.atula.doanapplication.ui.user.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atula.doanapplication.Interface.CallBackFirebase;
import com.atula.doanapplication.R;
import com.atula.doanapplication.config.DBFirebaseHelper;
import com.atula.doanapplication.model.CDoAn;
import com.atula.doanapplication.model.CUser;
import com.atula.doanapplication.ui.LoginActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class UpdateUserActivity extends AppCompatActivity implements ValueEventListener {
    TextView edt_Title;
    TextInputEditText input_Ten,input_NgaySinh,input_SDT;
    CUser cUserCurrent ;
    LinearLayout ln_NgaySinh;
    AutoCompleteTextView au_ngannh;
    Button btn_change;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView image_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        image_back = findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edt_Title = findViewById(R.id.edt_Title);
        input_Ten = findViewById(R.id.input_Ten);
        input_NgaySinh = findViewById(R.id.input_NgaySinh);
        input_SDT = findViewById(R.id.input_SDT);

        Intent intent = getIntent();
        String data = intent.getStringExtra("EXTRA");
        Gson gson = new Gson();
        cUserCurrent = gson.fromJson(data,CUser.class);
        edt_Title.setText(cUserCurrent.getMssv());
        input_Ten.setText(cUserCurrent.getTen());
        input_NgaySinh.setText(cUserCurrent.getNgaySinh());
        input_SDT.setText(cUserCurrent.getsDT());

        au_ngannh = findViewById(R.id.au_ngannh);
        String[] items = {"Công nghệ phần mềm","Hệ thống thông tin","Khoa học phân tích dữ liệu","Mạng máy tính"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(UpdateUserActivity.this,R.layout.list_item, items);
        au_ngannh.setAdapter(arrayAdapter);
        au_ngannh.setText(cUserCurrent.getChuyenNganh());

        ln_NgaySinh = findViewById(R.id.ln_NgaySinh);
        ln_NgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                DatePickerDialog picker;
                // date picker dialog
                picker = new DatePickerDialog(UpdateUserActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                input_NgaySinh.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);

                picker.show();
            }
        });

        btn_change = findViewById(R.id.btn_change);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = input_Ten.getText().toString();
                String ngaySinh = input_NgaySinh.getText().toString();
                String chuyenNganh = au_ngannh.getText().toString();
                String sDT = input_SDT.getText().toString();

                if(!TextUtils.isEmpty(ten) &&
                        !TextUtils.isEmpty(ngaySinh) &&
                        !TextUtils.isEmpty(chuyenNganh) &&
                        !TextUtils.isEmpty(sDT)){
                    cUserCurrent.setTen(ten);
                    cUserCurrent.setNgaySinh(ngaySinh);
                    cUserCurrent.setsDT(sDT);
                    cUserCurrent.setChuyenNganh(chuyenNganh);

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child(DBFirebaseHelper.TABLE_USER).child(cUserCurrent.getMssv()).setValue(cUserCurrent);


                    //update DoAn
                    if(cUserCurrent.getMaDoAn().length() != 0){
                        DatabaseReference da = firebaseDatabase.getReference()
                                .child(DBFirebaseHelper.TABLE_DOAN)
                                .child(cUserCurrent.getMaDoAn()).child("listUser");
                        da.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<CUser> arrayList = new ArrayList<>();
                                for (DataSnapshot messageSnapshot: snapshot.getChildren()) {
                                    CUser doAn = messageSnapshot.getValue(CUser.class);
                                    arrayList.add(doAn);
                                }
                                for(int i = 0 ; i < arrayList.size() ; i++){
                                    if(cUserCurrent.getMssv().equals(arrayList.get(i).getMssv())){
                                        arrayList.set(i,cUserCurrent);
                                        break;
                                    }
                                }
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                firebaseDatabase.getReference()
                                        .child(DBFirebaseHelper.TABLE_DOAN)
                                        .child(cUserCurrent.getMaDoAn()).child("listUser").setValue(arrayList);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    //UPDATE group
                    if(cUserCurrent.getNhomTruong().length() != 0){
                        DatabaseReference da = firebaseDatabase.getReference()
                                .child(DBFirebaseHelper.TABLE_GROUP)
                                .child(cUserCurrent.getNhomTruong());
                        da.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.getValue() != null){
                                    ArrayList<CUser> arrayList = new ArrayList<>();
                                    for (DataSnapshot messageSnapshot: snapshot.getChildren()) {
                                        CUser doAn = messageSnapshot.getValue(CUser.class);
                                        arrayList.add(doAn);
                                    }
                                    for(int i = 0 ; i < arrayList.size() ; i++){
                                        if(cUserCurrent.getMssv().equals(arrayList.get(i).getMssv())){
                                            arrayList.set(i,cUserCurrent);
                                            break;
                                        }
                                    }

                                    firebaseDatabase.getReference()
                                            .child(DBFirebaseHelper.TABLE_GROUP)
                                            .child(cUserCurrent.getNhomTruong()).setValue(arrayList);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }



                }else {
                    Toast.makeText(UpdateUserActivity.this, "Vui lòng nhập đầy đủ thông tin !", Toast.LENGTH_SHORT).show();

                }
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(DBFirebaseHelper.TABLE_USER).child(cUserCurrent.getMssv());
        databaseReference.addValueEventListener(this);
    }


    @Override
    public void onDataChange(@NonNull  DataSnapshot snapshot) {
        if(snapshot.getValue() != null){
            cUserCurrent = snapshot.getValue(CUser.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    edt_Title.setText(cUserCurrent.getMssv());
                    input_Ten.setText(cUserCurrent.getTen());
                    input_NgaySinh.setText(cUserCurrent.getNgaySinh());
                    input_SDT.setText(cUserCurrent.getsDT());
                    au_ngannh.setText(cUserCurrent.getChuyenNganh());
                }
            });

        }
    }

    @Override
    public void onCancelled(@NonNull  DatabaseError error) {

    }
}