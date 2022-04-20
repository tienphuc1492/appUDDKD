package com.atula.doanapplication.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.atula.doanapplication.Interface.CallBackFirebase;
import com.atula.doanapplication.config.DBFirebaseHelper;
import com.atula.doanapplication.config.Config;
import com.atula.doanapplication.ui.ad.activity.AdmindActivity;
import com.atula.doanapplication.ui.user.activity.MainActivity;
import com.atula.doanapplication.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class SignInFragment extends Fragment {
    public SignInFragment() { }
    private static SignInFragment instance;
    public static SignInFragment getInstance() {
        if (instance == null) {
            synchronized (SignInFragment.class) {
                instance = new SignInFragment();
            }
        }
        return instance;
    }
    View viewRoot;

    private View createView(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_sign_in, null, false);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return viewRoot;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewRoot = createView(savedInstanceState);
        //code init
        mapping();
    }

    TextInputEditText input_MSSV;
    EditText input_MK;
    Button btn_DNhap;
    RadioButton rdBtn1,rdBtn2;
    CheckBox ck_Save;

    void mapping(){
        rdBtn1 = viewRoot.findViewById(R.id.radioButton1);
        rdBtn2 = viewRoot.findViewById(R.id.radioButton2);

        input_MSSV = viewRoot.findViewById(R.id.input_MSSV);
        input_MK = viewRoot.findViewById(R.id.input_MK);
        btn_DNhap = viewRoot.findViewById(R.id.btn_DNhap);
        ck_Save = viewRoot.findViewById(R.id.ck_Save);
        btn_DNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mssv = input_MSSV.getText().toString();
                String pass = input_MK.getText().toString();
                if(!TextUtils.isEmpty(mssv) && !TextUtils.isEmpty(pass) ){


                    if(rdBtn1.isChecked()){
                        DBFirebaseHelper dbFirebaseHelper = DBFirebaseHelper.getInstance();
                        dbFirebaseHelper.login(mssv, pass, new CallBackFirebase() {
                            @Override
                            public void Successfull(String value) {
                                Config.getInstance().setSaveLogin(getContext(),ck_Save.isChecked(),mssv,pass,rdBtn1.isChecked());
                                Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.putExtra("MSSV",mssv);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            }

                            @Override
                            public void Error(String error) {
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else {
                        //check admin
                        if(mssv.equals(Config.TK_DANGNHAP_ADMIN) && pass.equals(Config.MK_DANGNHAP_ADMIN) ){
                            Config.getInstance().setSaveLogin(getContext(),ck_Save.isChecked(),mssv,pass,rdBtn1.isChecked());
                            Intent intent = new Intent(getActivity(), AdmindActivity.class);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        }else{
                            Toast.makeText(getActivity(), "Bạn không phải Admin", Toast.LENGTH_SHORT).show();
                        }

                    }


                }


            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        HashMap<String,String> map = Config.getInstance().getSaveLogin(getContext());
        String isSave = map.get("ISSAVE");
        if(isSave.equals("true")){
            ck_Save.setChecked(true);
            input_MSSV.setText(map.get("MSSV"));
            input_MK.setText(map.get("PASS"));
            boolean isSv = Boolean.parseBoolean(map.get("ISSV"));
            rdBtn1.setChecked(isSv);
            rdBtn2.setChecked(!isSv);
        }else{
            input_MSSV.setText("");
            input_MK.setText("");
        }

    }
}