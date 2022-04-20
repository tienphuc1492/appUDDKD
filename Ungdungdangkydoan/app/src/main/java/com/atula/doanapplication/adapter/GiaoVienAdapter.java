package com.atula.doanapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.atula.doanapplication.Interface.ItemClick;
import com.atula.doanapplication.R;
import com.atula.doanapplication.model.CDoAn;
import com.atula.doanapplication.model.GiaoVien;
import com.atula.doanapplication.ui.ad.activity.AdDetailActivity;

import java.util.ArrayList;

public class GiaoVienAdapter extends RecyclerView.Adapter<GiaoVienAdapter.CViewHoder>{
    Context context;
    ArrayList<GiaoVien> arrayList;
    ItemClick itemClick;

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public GiaoVienAdapter(Context context, ArrayList<GiaoVien> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_gv, parent, false);
        return new CViewHoder(v);

    }

    @Override
    public void onBindViewHolder(CViewHoder holder, int position) {
       GiaoVien giaoVien = arrayList.get(position);
       holder.txtChuyeNganh.setText(giaoVien.getTenChuyenNganh());
        holder.txtTen.setText(giaoVien.getTenGV());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    public  class CViewHoder extends RecyclerView.ViewHolder{
        TextView txtChuyeNganh,txtTen;
        CardView container;
        public CViewHoder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            txtChuyeNganh = itemView.findViewById(R.id.txtChuyeNganh);
            txtTen = itemView.findViewById(R.id.txtTen);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onItemClick(v,getAdapterPosition());
                }
            });
        }
    }

}
