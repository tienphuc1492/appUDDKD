package com.atula.doanapplication.ui.ad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.atula.doanapplication.Interface.DAClick;
import com.atula.doanapplication.Interface.IChooseFilter;
import com.atula.doanapplication.Interface.IChooseSpecialized;
import com.atula.doanapplication.Interface.ItemClick;
import com.atula.doanapplication.R;
import com.atula.doanapplication.adapter.DoAnAdapter;
import com.atula.doanapplication.config.DBFirebaseHelper;
import com.atula.doanapplication.model.CDoAn;
import com.atula.doanapplication.ui.LoginActivity;
import com.atula.doanapplication.ui.customview.CustomDialogAddDA;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AdmindActivity extends AppCompatActivity implements ValueEventListener {
    RecyclerView rcv_Data;
    ArrayList<CDoAn> arrayList;
    DoAnAdapter anAdapter;

    private Paint p = new Paint();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admind);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        rcv_Data = findViewById(R.id.rcv_Data);
        arrayList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdmindActivity.this, LinearLayoutManager.VERTICAL, false);
        anAdapter = new DoAnAdapter(AdmindActivity.this,arrayList);
//        anAdapter.setItemClick(new ItemClick() {
//            @Override
//            public void onItemClick(View v, int position) {
//                CDoAn cDoAn = arrayList.get(position);
//                switch (v.getId()){
//                    case R.id.ln_Diem:
//                        Log.d("onItemClick", "ln_Diem "+position);
//                        if (cDoAn.getTrangThai() == 1){
//                            CustomDialogAddDA customDialogAddDA =  new CustomDialogAddDA();
//                            customDialogAddDA.chamDiem(AdmindActivity.this, new IChooseSpecialized() {
//                                @Override
//                                public void specialized(String value) {
//                                    firebaseDatabase.getReference().child(DBFirebaseHelper.TABLE_DOAN).child(cDoAn.getKey()).child("diem").setValue(value);
//                                    Toast.makeText(AdmindActivity.this, "Hoàn thành", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }else{
//                            Toast.makeText(AdmindActivity.this, "Chưa có Sinh viên đăng kí đồ án này", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                        break;
//                    case R.id.ln_Sua:
//                        Log.d("onItemClick", "ln_Sua "+position);
//                        suaDoAn(cDoAn);
//                        break;
//                    case R.id.ln_Xoa:
//                        if(cDoAn.getTrangThai() == 1){
//                            Toast.makeText(AdmindActivity.this, "Không thể xoá !", Toast.LENGTH_SHORT).show();
//                        }else{
//                            DBFirebaseHelper dbFirebaseHelper = DBFirebaseHelper.getInstance();
//                            dbFirebaseHelper.removeDoAn(cDoAn.getKey());
//                        }
//                        break;
//                }
//            }
//        });

        anAdapter.setDaClick(new DAClick() {
            @Override
            public void onClick(View v, CDoAn cDoAn) {
                switch (v.getId()){
                    case R.id.ln_Diem:
                        if (cDoAn.getTrangThai() == 1){
                            CustomDialogAddDA customDialogAddDA =  new CustomDialogAddDA();
                            customDialogAddDA.chamDiem(AdmindActivity.this, new IChooseSpecialized() {
                                @Override
                                public void specialized(String value) {
                                    firebaseDatabase.getReference().child(DBFirebaseHelper.TABLE_DOAN).child(cDoAn.getKey()).child("diem").setValue(value);
                                    Toast.makeText(AdmindActivity.this, "Hoàn thành", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(AdmindActivity.this, "Chưa có Sinh viên đăng kí đồ án này", Toast.LENGTH_SHORT).show();
                        }


                        break;
                    case R.id.ln_Sua:

                        suaDoAn(cDoAn);
                        break;
                    case R.id.ln_Xoa:
                        if(cDoAn.getTrangThai() == 1){
                            Toast.makeText(AdmindActivity.this, "Không thể xoá !", Toast.LENGTH_SHORT).show();
                        }else{
                            DBFirebaseHelper dbFirebaseHelper = DBFirebaseHelper.getInstance();
                            dbFirebaseHelper.removeDoAn(cDoAn.getKey());
                        }
                        break;
                }
            }
        });

        rcv_Data.setLayoutManager(linearLayoutManager);
        rcv_Data.setAdapter(anAdapter);

        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private final ColorDrawable background = new ColorDrawable(getResources().getColor(R.color.colorBlue));

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                rcv_Data.post(new Runnable() {
                    public void run() {
                        anAdapter.showMenu(viewHolder.getAdapterPosition());
                    }
                });
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;

                if (dX > 0) {
                    background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX), itemView.getBottom());
                } else if (dX < 0) {
                    background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else {
                    background.setBounds(0, 0, 0, 0);
                }

                background.draw(c);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(rcv_Data);

        rcv_Data.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                rcv_Data.post(new Runnable() {
                    public void run() {
                        anAdapter.closeMenu();
                    }
                });
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("doan_table");
        databaseReference.addValueEventListener(this);

    }

    void suaDoAn(CDoAn doAn){
        Intent intent = new Intent(AdmindActivity.this,UpdateDoanActivity.class);
        Gson gson = new Gson();
        String convert = gson.toJson(doAn);
        intent.putExtra("EXTRA",convert);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (anAdapter.isMenuShown()) {
            anAdapter.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_ad, menu);


        MenuItem searchItem = menu.findItem(R.id.menuSearch);
        androidx.appcompat.widget.SearchView  searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                queryCurrent = s+"";
                anAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
    String queryCurrent = "";
    int id1 = -1;
    int id2 = -1;
    String filterTrangThai = "";
    String filterNganh = "";
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSearch:

                return true;
            case R.id.menuSort:
                CustomDialogAddDA cd2 = new CustomDialogAddDA();
                int idV1 = id1;
                int idV2 = id2;
                cd2.showDialogFilter(AdmindActivity.this,idV1,idV2 ,new IChooseFilter() {
                    @Override
                    public void specialized(String value, int idTrangThai,int idNganh) {
                        id1 = idTrangThai;
                        id2 = idNganh;
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
                                    if(nganh.contains("liệu")){
                                        nganh = "liệu";
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
                return true;
            case R.id.menuInsert:
                //show dialog
                CustomDialogAddDA cd = new CustomDialogAddDA();
                cd.showDialog(AdmindActivity.this, new IChooseSpecialized() {
                    @Override
                    public void specialized(String value) {
                        if(value.length() != 0){
                            Intent intent = new Intent(AdmindActivity.this,CreateDoanActivity.class);
                            intent.putExtra("title",value);
                            startActivity(intent);
                        }
                    }
                });
                return  true;
            case R.id.signout :
                Intent intent = new Intent(AdmindActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDataChange( DataSnapshot snapshot) {

        arrayList.clear();
        for (DataSnapshot messageSnapshot: snapshot.getChildren()) {
            String key = messageSnapshot.getKey().toString();

            CDoAn doAn = messageSnapshot.getValue(CDoAn.class);
            Log.d("onDataChange", doAn.toString());
            if(key.equals(anAdapter.getKeyShowMenu())){
                doAn.setShowMenu(true);
            }
            arrayList.add(doAn);

        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                anAdapter.notifyDataSetChanged();
                anAdapter.setFilterNganh(filterNganh);
                anAdapter.setFilterTrangThai(filterTrangThai);
                anAdapter.getFilter().filter(queryCurrent);
            }
        });
    }

    @Override
    public void onCancelled( DatabaseError error) {

    }
}