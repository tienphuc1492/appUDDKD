package com.atula.doanapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.atula.doanapplication.Interface.ItemClick;
import com.atula.doanapplication.R;
import com.atula.doanapplication.model.CUser;
import com.atula.doanapplication.model.GiaoVien;

import java.util.ArrayList;

public class SVAdapter extends RecyclerView.Adapter<SVAdapter.CViewHoder>{
    Context context;
    ArrayList<CUser> arrayList;
    ItemClick itemClick;

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public SVAdapter(Context context, ArrayList<CUser> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_sv, parent, false);
        return new CViewHoder(v);

    }

    @Override
    public void onBindViewHolder(CViewHoder holder, int position) {
        CUser giaoVien = arrayList.get(position);
       holder.txt_mssv.setText(giaoVien.getMssv());
        holder.txtTen.setText(giaoVien.getTen());
        holder.txt_sdt.setText(giaoVien.getsDT());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    public  class CViewHoder extends RecyclerView.ViewHolder{
        TextView txt_sdt,txtTen,txt_mssv;
        CardView container;
        public CViewHoder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            txt_sdt = itemView.findViewById(R.id.txt_sdt);
            txt_mssv = itemView.findViewById(R.id.txt_mssv);
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
