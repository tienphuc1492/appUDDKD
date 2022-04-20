package com.atula.doanapplication.ui.user.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.atula.doanapplication.R;
import com.atula.doanapplication.config.DBFirebaseHelper;
import com.atula.doanapplication.config.InfoUser;
import com.atula.doanapplication.model.CUser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePassActivity extends AppCompatActivity {
    TextInputEditText input_MSSV;
    EditText input_MK,input_MK1,input_MK2;
    Button btn_DNhap;
    ImageView image_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        image_back = findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        input_MSSV = findViewById(R.id.input_MSSV);
        input_MK = findViewById(R.id.input_MK);
        input_MK1 = findViewById(R.id.input_MK1);
        input_MK2 = findViewById(R.id.input_MK2);
        btn_DNhap = findViewById(R.id.btn_DNhap);
        btn_DNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mssv = input_MSSV.getText().toString();
                String mk = input_MK.getText().toString();
                String mk1 = input_MK1.getText().toString();
                String mk2 = input_MK2.getText().toString();
                if(!TextUtils.isEmpty(mssv) &&
                        !TextUtils.isEmpty(mk) &&
                        !TextUtils.isEmpty(mk1) &&
                        !TextUtils.isEmpty(mk2) ){
                    CUser cUser = InfoUser.getInstance().getcUser();
                    if(mssv.equals(cUser.getMssv())){
                        if(cUser.getmK().equals(mk)){
                            if(mk1.equals(mk2)){
                                InfoUser.getInstance().getcUser().setmK(mk);
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                firebaseDatabase.getReference().child(DBFirebaseHelper.TABLE_USER).child(mssv).child("mK").setValue(mk1);
                                Toast.makeText(ChangePassActivity.this, "Hoàn tất thay đổi mật khẩu ", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(ChangePassActivity.this, "2 Mật khẩu mới phải trùng nhau !", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(ChangePassActivity.this, "Mật khẩu hiện tại không đúng ", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(ChangePassActivity.this, "MSSV không đúng ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}