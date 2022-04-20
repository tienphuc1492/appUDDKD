package com.atula.doanapplication.ui.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atula.doanapplication.Interface.ItemClick;
import com.atula.doanapplication.adapter.SVAdapter;
import com.atula.doanapplication.config.DBFirebaseHelper;
import com.atula.doanapplication.model.CDoAn;
import com.atula.doanapplication.ui.user.activity.MainActivity;
import com.atula.doanapplication.R;
import com.atula.doanapplication.config.InfoUser;
import com.atula.doanapplication.model.CUser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PageTwoFragment extends Fragment  implements ValueEventListener {
    public PageTwoFragment() { }
    private static PageTwoFragment instance;
    public static PageTwoFragment getInstance() {
        if (instance == null) {
            synchronized (PageTwoFragment.class) {
                instance = new PageTwoFragment();
            }
        }
        return instance;
    }
    View viewRoot;

    private View createView(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_page2, null, false);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Thông tin đồ án đăng kí");
        return viewRoot;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewRoot = createView(savedInstanceState);
        //code init
        firebaseDatabase = FirebaseDatabase.getInstance();
        mapping();

    }

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LinearLayout ln_dki;
    Button btn_Dki;
    FrameLayout fr_layout;
    TextView txt_huy,txt_Title;
    EditText txt_gv,txt_Mota,txt_moitruong,txt_thoigian,txt_trangthai,txt_chuyenganh,txt_Dki,txt_diem;
    LinearLayout ln_sv;
    CDoAn cDoAnCurrent;

    RecyclerView rcv_dsSV;
    ArrayList<CUser> arrDSSV;
    SVAdapter svAdapter;
    void mapping(){
        ln_dki = viewRoot.findViewById(R.id.ln_dki);
        btn_Dki = viewRoot.findViewById(R.id.btn_Dki);
        btn_Dki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = MainActivity.getInstance();
                mainActivity.swapToHome();
            }
        });
        fr_layout = viewRoot.findViewById(R.id.fr_layout);
        txt_Title = viewRoot.findViewById(R.id.txt_Title);
        txt_gv = viewRoot.findViewById(R.id.txt_gv);
        txt_Mota = viewRoot.findViewById(R.id.txt_Mota);
        txt_moitruong = viewRoot.findViewById(R.id.txt_moitruong);
        txt_thoigian = viewRoot.findViewById(R.id.txt_thoigian);
        txt_trangthai = viewRoot.findViewById(R.id.txt_trangthai);
        txt_chuyenganh = viewRoot.findViewById(R.id.txt_chuyenganh);
        txt_diem = viewRoot.findViewById(R.id.txt_diem);
        txt_Dki = viewRoot.findViewById(R.id.txt_Dki);
        ln_sv = viewRoot.findViewById(R.id.ln_sv);

        rcv_dsSV = viewRoot.findViewById(R.id.rcv_dsSV);
        arrDSSV = new ArrayList<>();
        svAdapter = new SVAdapter(getContext(),arrDSSV);
        svAdapter.setItemClick(new ItemClick() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_dsSV.setLayoutManager(linearLayoutManager);
        rcv_dsSV.setAdapter(svAdapter);

        txt_huy = viewRoot.findViewById(R.id.txt_huy);
        txt_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cDoAnCurrent.getDiem().length() != 0){
                    Toast.makeText(getActivity(), "Không thể huỷ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cDoAnCurrent != null){
                    cDoAnCurrent.setListUser(null);
                    cDoAnCurrent.setTrangThai(0);
                    firebaseDatabase.getReference().child(DBFirebaseHelper.TABLE_DOAN).child(cDoAnCurrent.getKey()).setValue(cDoAnCurrent);
                    InfoUser.getInstance().getcUser().setMaDoAn("");

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
                                    firebaseDatabase.getReference()
                                            .child(DBFirebaseHelper.TABLE_USER)
                                            .child(InfoUser.getInstance().getcUser().getMssv()).setValue(InfoUser.getInstance().getcUser());

                                }else{
                                    //da co nhom
                                    ArrayList<CUser> list = new ArrayList<>();
                                    for (DataSnapshot messageSnapshot: snapshot.getChildren()) {
                                        CUser c = messageSnapshot.getValue(CUser.class);
                                        c.setMaDoAn("");
                                        list.add(c);
                                    }


                                    InfoUser.getInstance().getcUser().setMaDoAn("");
                                    for(CUser cu : list){
                                        firebaseDatabase.getReference().
                                                child(DBFirebaseHelper.TABLE_USER).
                                                child(cu.getMssv()).
                                                child("maDoAn").
                                                setValue("");
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

                loadData();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    void loadData(){
        CUser cUser = InfoUser.getInstance().getcUser();
        if(cUser.getMaDoAn().length() == 0 ){
            ln_dki.setVisibility(View.VISIBLE);
            fr_layout.setVisibility(View.GONE);
            try {
                if(databaseReference != null){
                    databaseReference.removeEventListener(this);
                }
            }catch (Exception e){

            }
        }else{
            ln_dki.setVisibility(View.GONE);
            fr_layout.setVisibility(View.VISIBLE);
            String keyDa = cUser.getMaDoAn();
            databaseReference = firebaseDatabase.getReference().child(DBFirebaseHelper.TABLE_DOAN).child(keyDa);
            databaseReference.addValueEventListener(this);
        }
        if(!cUser.checkIsNhomTruong()){
            txt_huy.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDataChange(@NonNull  DataSnapshot snapshot) {
        if(snapshot.getValue() != null){
            CDoAn cDoAn = snapshot.getValue(CDoAn.class);
            cDoAnCurrent = cDoAn;
            if(getActivity() != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt_Title.setText(cDoAn.getTenDA());
                        txt_chuyenganh.setText(cDoAn.getChuyeNganh());
                        txt_thoigian.setText(cDoAn.getThoiGian());
                        txt_moitruong.setText(cDoAn.getMoiTruong());
                        txt_Mota.setText(cDoAn.getMucTieuDetai());
                        txt_trangthai.setText(cDoAn.getTrangThaiString());
                        txt_diem.setText(cDoAn.getDiem());
                        txt_gv.setText(cDoAn.getGiaoVien().showInfo());

                        arrDSSV.clear();
                        try{
                            for(int i = 0 ; i < cDoAn.getListUser().size() ; i++){
                                CUser us = cDoAn.getListUser().get(i);
                                arrDSSV.add(us);
                            }
                        }catch (Exception e){
                        }
                        svAdapter.notifyDataSetChanged();
                    }
                });
            }

        }
    }

    @Override
    public void onCancelled(@NonNull  DatabaseError error) {

    }
}