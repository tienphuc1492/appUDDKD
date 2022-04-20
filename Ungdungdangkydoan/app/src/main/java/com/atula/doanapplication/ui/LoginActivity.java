package com.atula.doanapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.atula.doanapplication.R;
import com.atula.doanapplication.config.DBFirebaseHelper;
import com.atula.doanapplication.ui.fragment.SignInFragment;
import com.atula.doanapplication.ui.fragment.SignUpFragment;

public class LoginActivity extends AppCompatActivity {
    //Instance
    private static LoginActivity activity;
    public static LoginActivity getInstance(){
        if(activity == null){
            activity = new LoginActivity();
        }
        return activity;
    }


    DBFirebaseHelper dbFirebaseHelper;
    int pageCurrnent = 0;
    TextView  txt_DangKi,txt_DangNhap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;

        dbFirebaseHelper = DBFirebaseHelper.getInstance();

        mapping();
        loadFragment(1);
        loadFragment(pageCurrnent);
    }


    void  mapping(){
        txt_DangKi = findViewById(R.id.txt_DangKi);
        txt_DangKi.setText("Đăng nhập");
        txt_DangNhap = findViewById(R.id.txt_DangNhap);
        txt_DangNhap.setText("Đăng kí");
        txt_DangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageCurrnent != 0){
                    Typeface tf = txt_DangKi.getTypeface();
                    txt_DangKi.setTypeface(tf, Typeface.BOLD);
                    txt_DangNhap.setTypeface(tf ,Typeface.NORMAL);
                    pageCurrnent = 0;
                    loadFragment(pageCurrnent);
                }
            }
        });
        txt_DangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageCurrnent != 1){
                    Typeface tf = txt_DangNhap.getTypeface();
                    txt_DangNhap.setTypeface(tf, Typeface.BOLD);
                    txt_DangKi.setTypeface(tf, Typeface.NORMAL);
                    pageCurrnent = 1;
                    loadFragment(pageCurrnent);
                }
            }
        });
    }
    public void buttonDangNhapClick(String userName){
        if(pageCurrnent != 0){
            Typeface tf = txt_DangKi.getTypeface();
            txt_DangKi.setTypeface(tf, Typeface.BOLD);
            txt_DangNhap.setTypeface(tf ,Typeface.NORMAL);
            pageCurrnent = 0;
            loadFragment(pageCurrnent);
        }
    }
    public boolean loadFragment(int id) {
        Fragment fragment = null;
        String backStateName = "SignInFragment";
        switch (id) {
            case 0:
                fragment = SignInFragment.getInstance();

                break;
            case 1:
                fragment = SignUpFragment.getInstance();
                backStateName = "SignUpFragment";
                break;
        }
        replaceFragment(fragment,backStateName,id);
        return true;
    }
    public void replaceFragment (Fragment fragment, String backStateName, int i){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentByTag(backStateName) == null){
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }else{
            getSupportFragmentManager().popBackStack(backStateName,i);
        }
    }

}