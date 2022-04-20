package com.atula.doanapplication.ui.user.fragment;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atula.doanapplication.Interface.IChooseFilter;
import com.atula.doanapplication.Interface.IChooseSpecialized;
import com.atula.doanapplication.Interface.ItemClick;
import com.atula.doanapplication.adapter.DoAnAdapter;
import com.atula.doanapplication.config.DBFirebaseHelper;
import com.atula.doanapplication.model.CDoAn;
import com.atula.doanapplication.ui.ad.activity.AdmindActivity;
import com.atula.doanapplication.ui.customview.CustomDialogFilterFragment;
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
import java.util.Collections;

public class PageOneFragment extends Fragment implements ValueEventListener {
    public PageOneFragment() { }
    private static PageOneFragment instance;
    public static PageOneFragment getInstance() {
        if (instance == null) {
            synchronized (PageOneFragment.class) {
                instance = new PageOneFragment();
            }
        }
        return instance;
    }
    View viewRoot;

    private View createView(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_page1, null, false);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Danh sách đồ án");
        return viewRoot;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewRoot = createView(savedInstanceState);
        //code init
        mapping();
    }

    RecyclerView rcv_Data;
    ArrayList<CDoAn> arrayList;
    DoAnAdapter anAdapter;

    private Paint p = new Paint();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    void mapping(){
        rcv_Data = viewRoot.findViewById(R.id.rcv_Data);
        arrayList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        anAdapter = new DoAnAdapter(getActivity(),arrayList,true);

        rcv_Data.setLayoutManager(linearLayoutManager);
        rcv_Data.setAdapter(anAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("doan_table");
        databaseReference.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        arrayList.clear();
        for (DataSnapshot messageSnapshot: snapshot.getChildren()) {
            String key = messageSnapshot.getKey().toString();


            CDoAn doAn = (CDoAn) messageSnapshot.getValue(CDoAn.class);
         //   Log.d("Phong", messageSnapshot.toString());
            if(key.equals(anAdapter.getKeyShowMenu())){
                doAn.setShowMenu(true);
            }
            arrayList.add(doAn);

        }
        if(getActivity()!=null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    anAdapter.notifyDataSetChanged();

                    anAdapter.setFilterNganh(filterNganh);
                    anAdapter.setFilterTrangThai(filterTrangThai);
                    anAdapter.getFilter().filter(queryCurrent);
                }
            });
        }

    }

    @Override
    public void onCancelled(DatabaseError error) {

    }

    String queryCurrent = "";

    //Tìm kiếm
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu_user, menu);
        MenuItem itemSearch = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) itemSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                anAdapter.getFilter().filter(s);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    int id1 = -1;
    int id2 = -1;
    String filterTrangThai = "";
    String filterNganh = "";
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuSort){
            //
            CustomDialogFilterFragment customDialogFilterFragment = new CustomDialogFilterFragment(id1,id2);
            customDialogFilterFragment.setInput(new IChooseFilter() {
                @Override
                public void specialized(String value,int idV1,int idV2) {
                    id1 = idV1;
                    id2 = idV2;
                    String[] va = value.split(",");
                    //format;
                    String trangthai = va[0];

                    if(trangthai.contains("Chưa")){
                        trangthai = "Chưa";
                    }else{
                        if(trangthai.contains("Đã")){
                            trangthai = "Đã";
                        }else{
                            trangthai = "";
                        }

                    }

                    String nganh = va[1];
                    if(nganh.contains("mềm")){
                        nganh = "mềm";
                    }else {
                        if(nganh.contains("tin")){
                            nganh = "tin";
                        }else{
                            if(nganh.contains("tính")){
                                nganh = "tính";
                            }else {
                                if(nganh.contains("toàn")){
                                    nganh = "toàn";
                                }else{
                                    nganh = "";
                                }

                            }
                        }
                    }

                    filterNganh = nganh;
                    filterTrangThai = trangthai;

                    anAdapter.setFilterNganh(nganh);
                    anAdapter.setFilterTrangThai(trangthai);
                    anAdapter.getFilter().filter(queryCurrent);
                }
            });
            customDialogFilterFragment.show(getFragmentManager(),"customDialogFilterFragment");
        }
        return super.onOptionsItemSelected(item);

    }


}