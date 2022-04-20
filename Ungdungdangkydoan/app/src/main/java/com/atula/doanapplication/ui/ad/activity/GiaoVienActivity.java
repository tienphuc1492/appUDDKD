package com.atula.doanapplication.ui.ad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.atula.doanapplication.Interface.ItemClick;
import com.atula.doanapplication.R;
import com.atula.doanapplication.adapter.GiaoVienAdapter;
import com.atula.doanapplication.model.GiaoVien;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GiaoVienActivity extends AppCompatActivity {
    RecyclerView rcv_gv;
    ArrayList<GiaoVien> arrayList;
    GiaoVienAdapter adapter;

    public static final String EXTRA_DATA = "EXTRA_DATA";
    ImageView image_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_vien);
        image_back = findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        arrayList = new ArrayList<>();
        rcv_gv = findViewById(R.id.rcv_gv);
        adapter = new GiaoVienAdapter(GiaoVienActivity.this,arrayList);
        adapter.setItemClick(new ItemClick() {
            @Override
            public void onItemClick(View v, int position) {
                GiaoVien giaoVien = arrayList.get(position);
                final Intent data = new Intent();
                Gson gson = new Gson();
                String value = gson.toJson(giaoVien);
                data.putExtra(EXTRA_DATA, value);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GiaoVienActivity.this, LinearLayoutManager.VERTICAL, false);
        rcv_gv.setAdapter(adapter);
        rcv_gv.setLayoutManager(linearLayoutManager);

        //load danhsach
        getData();
    }
    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    void getData(){
        try {
            String jsonGV = readText(getApplicationContext(), R.raw.giaovien);
            JSONArray jsonArrayGV = new JSONArray(jsonGV);
            for (int i = 0 ; i < jsonArrayGV.length() ; i++){
                JSONObject jsonObject = jsonArrayGV.getJSONObject(i);
                String MaChuyeNganh = jsonObject.getString("TenChuyeNganh");
                String TenGV = jsonObject.getString("TenGV");
                String MaGV = jsonObject.getString("MaGV");
                String Email = jsonObject.getString("Email");
                arrayList.add(new GiaoVien(MaGV,TenGV,Email,MaChuyeNganh));
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String readText(Context context, int resId) throws IOException {
        InputStream is = context.getResources().openRawResource(resId);
        BufferedReader br= new BufferedReader(new InputStreamReader(is));
        StringBuilder sb= new StringBuilder();
        String s= null;
        while((  s = br.readLine())!=null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

}