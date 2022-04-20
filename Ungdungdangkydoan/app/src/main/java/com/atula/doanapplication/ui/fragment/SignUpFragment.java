package com.atula.doanapplication.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.atula.doanapplication.Interface.CallBackFirebase;
import com.atula.doanapplication.Interface.IChooseSpecialized;
import com.atula.doanapplication.R;
import com.atula.doanapplication.config.DBFirebaseHelper;
import com.atula.doanapplication.model.CUser;
import com.atula.doanapplication.ui.LoginActivity;
import com.atula.doanapplication.ui.customview.CustomDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

public class SignUpFragment extends Fragment {
    public SignUpFragment() { }
    private static SignUpFragment instance;
    public static SignUpFragment getInstance() {
        if (instance == null) {
            synchronized (SignUpFragment.class) {
                instance = new SignUpFragment();
            }
        }
        return instance;
    }
    View viewRoot;

    private View createView(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_sign_up, null, false);
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


    LinearLayout ln_ChuyenNganh,ln_NgaySinh;
    com.google.android.material.textfield.TextInputLayout edt_ChuyenNganh, edt_NgaySinh;
    EditText input_MK;
    TextInputEditText input_MSSV,input_Ten,input_SDT;
    Button btn_Dki;
    void mapping(){
        edt_ChuyenNganh = viewRoot.findViewById(R.id.input_ChuyenNganh);
        ln_ChuyenNganh = viewRoot.findViewById(R.id.ln_ChuyenNganh);
        ln_ChuyenNganh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogFragment df = new CustomDialogFragment();
                df.setInput(new IChooseSpecialized() {
                    @Override
                    public void specialized(String value) {
                        edt_ChuyenNganh.getEditText().setText(value);
                    }
                });
                df.show(getFragmentManager(),"CustomDialogFragment");
            }
        });

        edt_NgaySinh = viewRoot.findViewById(R.id.input_NgaySinh);
        edt_NgaySinh.getEditText().setInputType(InputType.TYPE_NULL);
        ln_NgaySinh = viewRoot.findViewById(R.id.ln_NgaySinh);
        ln_NgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                DatePickerDialog picker;
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edt_NgaySinh.getEditText().setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);

                picker.show();
            }
        });

        input_MSSV = viewRoot.findViewById(R.id.input_MSSV);
        input_Ten = viewRoot.findViewById(R.id.input_Ten);
        input_SDT = viewRoot.findViewById(R.id.input_SDT);
        input_MK = viewRoot.findViewById(R.id.input_MK);

        btn_Dki = viewRoot.findViewById(R.id.btn_Dki);
        btn_Dki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mssv = input_MSSV.getText().toString();
                String ten = input_Ten.getText().toString();
                String ngaySinh = edt_NgaySinh.getEditText().getText().toString();
                String chuyenNganh = edt_ChuyenNganh.getEditText().getText().toString();
                String sDT = input_SDT.getText().toString();
                String mK = input_MK.getText().toString();
                if(!TextUtils.isEmpty(mssv) &&
                        !TextUtils.isEmpty(ten) &&
                        !TextUtils.isEmpty(ngaySinh) &&
                        !TextUtils.isEmpty(chuyenNganh) &&
                        !TextUtils.isEmpty(sDT) &&
                        !TextUtils.isEmpty(mK) ){
                    CUser user = new CUser(mssv,ten,ngaySinh,chuyenNganh,sDT,mK);
                    DBFirebaseHelper firebaseHelper = DBFirebaseHelper.getInstance();

                    firebaseHelper.createUser(user, new CallBackFirebase() {
                        @Override
                        public void Successfull(String value) {
                            if(getActivity() != null){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        input_MSSV.setText("");
                                        input_Ten.setText("");
                                        edt_NgaySinh.getEditText().setText("");
                                        edt_ChuyenNganh.getEditText().setText("");
                                        input_SDT.setText("");
                                        input_MK.setText("");
                                        Toast.makeText(getActivity(), value, Toast.LENGTH_SHORT).show();
                                        LoginActivity loginActivity = LoginActivity.getInstance();
                                        loginActivity.buttonDangNhapClick(mssv);
                                    }
                                });
                            }

                        }

                        @Override
                        public void Error(String error) {
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin !", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}