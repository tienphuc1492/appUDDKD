package com.atula.doanapplication.ui.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atula.doanapplication.Interface.IChooseSpecialized;
import com.atula.doanapplication.Interface.ItemClick;
import com.atula.doanapplication.adapter.SVAdapter;
import com.atula.doanapplication.config.DBFirebaseHelper;
import com.atula.doanapplication.ui.LoginActivity;
import com.atula.doanapplication.ui.ad.activity.UpdateDoanActivity;
import com.atula.doanapplication.ui.customview.DialogAddMssvFragment;
import com.atula.doanapplication.ui.user.activity.ChangePassActivity;
import com.atula.doanapplication.ui.user.activity.MainActivity;
import com.atula.doanapplication.R;
import com.atula.doanapplication.config.InfoUser;
import com.atula.doanapplication.model.CUser;
import com.atula.doanapplication.ui.user.activity.ThemThanhVienActivity;
import com.atula.doanapplication.ui.user.activity.UpdateUserActivity;
import com.atula.doanapplication.ui.user.activity.XoaThanhVienActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PageThreeFragment extends Fragment implements ValueEventListener {
    public PageThreeFragment() { }
    private static PageThreeFragment instance;
    public static PageThreeFragment getInstance() {
        if (instance == null) {
            synchronized (PageThreeFragment.class) {
                instance = new PageThreeFragment();
            }
        }
        return instance;
    }
    View viewRoot;

    private View createView(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_page3, null, false);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Thông tin cá nhân");
        return viewRoot;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewRoot = createView(savedInstanceState);
        //code init
        firebaseDatabase = FirebaseDatabase.getInstance();
        mapping();
        loadData();
    }

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText txt_Ten,txt_mssv,txt_chuyenganh,txt_ngaysinh,txt_sdt;
    LinearLayout ln_ThemTV;

    RecyclerView rcv_dsSV;
    ArrayList<CUser> arrDSSV;
    SVAdapter svAdapter;

    void mapping(){
        txt_Ten = viewRoot.findViewById(R.id.txt_Ten);
        txt_mssv = viewRoot.findViewById(R.id.txt_mssv);
        txt_chuyenganh = viewRoot.findViewById(R.id.txt_chuyenganh);
        txt_ngaysinh = viewRoot.findViewById(R.id.txt_ngaysinh);
        txt_sdt = viewRoot.findViewById(R.id.txt_sdt);


        ln_ThemTV = viewRoot.findViewById(R.id.ln_ThemTV);
        ln_ThemTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddMssvFragment dialogAddMssvFragment = new DialogAddMssvFragment();
                dialogAddMssvFragment.setInput(new IChooseSpecialized() {
                    @Override
                    public void specialized(String value) {
                        if(!value.equals(cUserCurrent.getMssv())){
                            Intent intent = new Intent(getActivity(), ThemThanhVienActivity.class);
                            intent.putExtra("MSSV",value);
                            Gson gson = new Gson();
                            String user = gson.toJson(cUserCurrent);
                            intent.putExtra("EXTRA",user);
                            getActivity().startActivity(intent);
                        }else
                        {
                            Toast.makeText(getActivity(), "Không thể tự thêm bản thân vào nhóm ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialogAddMssvFragment.show(getFragmentManager(),"dialogAddMssvFragment");
            }
        });

        rcv_dsSV = viewRoot.findViewById(R.id.rcv_dsSV);
        arrDSSV = new ArrayList<>();
        svAdapter = new SVAdapter(getContext(),arrDSSV);
        svAdapter.setItemClick(new ItemClick() {
            @Override
            public void onItemClick(View v, int position) {
                if(cUserCurrent.checkIsNhomTruong()){
                    String value = arrDSSV.get(position).getMssv();
                    if(!value.equals(cUserCurrent.getMssv())){
                        Intent intent = new Intent(getActivity(), XoaThanhVienActivity.class);
                        intent.putExtra("MSSV",value);
                        Gson gson = new Gson();
                        String user = gson.toJson(cUserCurrent);
                        intent.putExtra("EXTRA",user);
                        getActivity().startActivity(intent);
                    }

                }else{
                    Toast.makeText(getContext(), "Bạn không phải nhóm trưởng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_dsSV.setLayoutManager(linearLayoutManager);
        rcv_dsSV.setAdapter(svAdapter);
    }

    void loadData(){
        databaseReference = firebaseDatabase.getReference()
                .child(DBFirebaseHelper.TABLE_USER)
                .child(InfoUser.getInstance().getcUser().getMssv());
        databaseReference.addValueEventListener(this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu_infouser, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuOut){
            //
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        if(item.getItemId() == R.id.menuPass){
            if(cUserCurrent!=null){
                Intent intent = new Intent(getActivity(), ChangePassActivity.class);
                Gson gson = new Gson();
                String value = gson.toJson(cUserCurrent);
                intent.putExtra("EXTRA",value);
                getActivity().startActivity(intent);
            }

        }
        if(item.getItemId() == R.id.menuEdit){
            //
            if(cUserCurrent!=null){
                Intent intent = new Intent(getActivity(), UpdateUserActivity.class);
                Gson gson = new Gson();
                String value = gson.toJson(cUserCurrent);
                intent.putExtra("EXTRA",value);
                getActivity().startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
    CUser cUserCurrent ;
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.getValue() != null){
            CUser cUser = snapshot.getValue(CUser.class);
            cUserCurrent = cUser;
            if(getActivity() != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt_chuyenganh.setText(cUser.getChuyenNganh());
                        txt_mssv.setText(cUser.getMssv());
                        txt_Ten.setText(cUser.getTen());
                        txt_ngaysinh.setText(cUser.getNgaySinh());
                        txt_sdt.setText(cUser.getsDT());

                        if(cUserCurrent.getNhomTruong().length() != 0 ){
                            //da co nhom
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference d = firebaseDatabase.getReference().child(DBFirebaseHelper.TABLE_GROUP).child(cUserCurrent.getNhomTruong());
                            d.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getValue() != null){
                                        if(getActivity() != null){
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(snapshot.getChildrenCount() >= 3){
                                                        ln_ThemTV.setVisibility(View.GONE);
                                                    }else {
                                                        if(cUserCurrent.getNhomTruong().length() == 0 ){
                                                            ln_ThemTV.setVisibility(View.VISIBLE);
                                                        }else{
                                                            if(cUserCurrent.getNhomTruong().equals(cUserCurrent.getMssv())){
                                                                ln_ThemTV.setVisibility(View.VISIBLE);
                                                            }else{
                                                                ln_ThemTV.setVisibility(View.GONE);
                                                            }
                                                        }
                                                    }

                                                  //  String temp = "\n";
                                                    arrDSSV.clear();
                                                    for (DataSnapshot messageSnapshot: snapshot.getChildren()) {
                                                        CUser cc = messageSnapshot.getValue(CUser.class);
                                                        arrDSSV.add(cc);
                                                        //temp = temp + cc.getTen() + "\n\t" + cc.getMssv() + "\n\t" + cc.getsDT()+"\n\n";

                                                    }
//                                                    txt_DSSV.setVisibility(View.VISIBLE);
//                                                    txt_DSSV.setText(temp);
                                                    rcv_dsSV.setVisibility(View.VISIBLE);
                                                    svAdapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull  DatabaseError error) {

                                }
                            });


                        }
                        else{
                            if(cUserCurrent.getNhomTruong().length() == 0 ){
                                ln_ThemTV.setVisibility(View.VISIBLE);
                            }else{
                                if(cUserCurrent.getNhomTruong().equals(cUserCurrent.getMssv())){
                                    ln_ThemTV.setVisibility(View.VISIBLE);
                                }else{
                                    ln_ThemTV.setVisibility(View.GONE);
                                }
                            }

                            rcv_dsSV.setVisibility(View.GONE);
                        }
                    }
                });
            }



        }
    }

    @Override
    public void onCancelled(@NonNull  DatabaseError error) {

    }
}