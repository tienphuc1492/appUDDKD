package com.atula.doanapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.atula.doanapplication.Interface.DAClick;
import com.atula.doanapplication.Interface.ItemClick;
import com.atula.doanapplication.R;
import com.atula.doanapplication.model.CDoAn;
import com.atula.doanapplication.ui.ad.activity.AdDetailActivity;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DoAnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    Context context;
    ArrayList<CDoAn> arrayList;
    ArrayList<CDoAn> arrayListFillter;
    boolean isUser = false;

    private final int SHOW_MENU = 1;
    private final int HIDE_MENU = 2;

    @Override
    public int getItemViewType(int position) {
        if(arrayList.get(position).isShowMenu()){
            return SHOW_MENU;
        }else{
            return HIDE_MENU;
        }
    }

    public DoAnAdapter(Context context, ArrayList<CDoAn> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListFillter = arrayList;
    }
    public DoAnAdapter(Context context, ArrayList<CDoAn> arrayList,boolean isUser) {
        this.isUser = isUser;
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListFillter = arrayList;

    }

    DAClick daClick;

    public void setDaClick(DAClick daClick) {
        this.daClick = daClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType==SHOW_MENU){
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_menu, parent, false);
            return new MenuViewHolder(v);
        }else{
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_doan_ad, parent, false);
            return new DAViewHoder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CDoAn entity = arrayList.get(position);
        if(holder instanceof DAViewHoder){
            ((DAViewHoder)holder).txt_Ten.setText(entity.getTenDA());
            ((DAViewHoder)holder).txt_Nganh.setText(entity.getChuyeNganh());
            ((DAViewHoder)holder).txt_gv.setText(entity.getGiaoVien().getTenGV());
            ((DAViewHoder)holder).txt_trangthai.setText(entity.getTrangThaiString());

            ((DAViewHoder)holder).container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(!isUser)
                        showMenu(position);
                    return true;
                }
            });
            ((DAViewHoder)holder).container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AdDetailActivity.class);
                    Gson gson = new Gson();
                    String value = gson.toJson(entity);
                    intent.putExtra("EXTRA",value);
                    intent.putExtra("ISUSER",isUser);
                    context.startActivity(intent);
                }
            });
        }
        if(holder instanceof MenuViewHolder){
            //Menu Actions
            ((MenuViewHolder)holder).txt_Title.setText(entity.getTenDA());
            ((MenuViewHolder)holder).ln_Diem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daClick.onClick(v,entity);
                }
            });
            ((MenuViewHolder)holder).ln_Xoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daClick.onClick(v,entity);
                }
            });
            ((MenuViewHolder)holder).ln_Sua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daClick.onClick(v,entity);
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public void showMenu(int position) {
        for(int i=0; i<arrayList.size(); i++){
            arrayList.get(i).setShowMenu(false);
        }
        arrayList.get(position).setShowMenu(true);
        keyShowMenu = arrayList.get(position).getKey();
        notifyDataSetChanged();
    }
    String keyShowMenu = "";

    public String getKeyShowMenu() {
        return keyShowMenu;
    }

    public boolean isMenuShown() {
        for(int i=0; i<arrayList.size(); i++){
            if(arrayList.get(i).isShowMenu()){
                return true;
            }
        }
        return false;
    }

    public void closeMenu() {
        for(int i=0; i<arrayList.size(); i++){
            arrayList.get(i).setShowMenu(false);
        }
        keyShowMenu = "";
        notifyDataSetChanged();
    }

    String filterTrangThai = "";
    String filterNganh = "";

    public void setFilterTrangThai(String filterTrangThai) {
        this.filterTrangThai = filterTrangThai;
    }

    public void setFilterNganh(String filterNganh) {
        this.filterNganh = filterNganh;
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                //charSequence -->giá trị nhập vào
                if (charSequence == null || charSequence.length() == 0) {

                    filterResults.values = arrayListFillter;
                    filterResults.count = arrayListFillter.size();

                    ArrayList<CDoAn> Arrtemp1 = new ArrayList<>();
                    ArrayList<CDoAn> Arrtemp = new ArrayList<>();

                    if(filterTrangThai.length() != 0){
                        for (CDoAn _da : arrayListFillter) {
                            if ( _da.getTrangThaiString().toLowerCase().contains(filterTrangThai.toLowerCase())) {
                                Arrtemp1.add(_da);
                            }
                        }
                    }else{
                        Arrtemp1.addAll(arrayListFillter);
                    }
                    if(filterNganh.length() != 0){
                        for (CDoAn _da : Arrtemp1) {
                            if ( _da.getChuyeNganh().toLowerCase().contains(filterNganh.toLowerCase())) {
                                Arrtemp.add(_da);
                            }
                        }
                    }else {
                        Arrtemp.addAll(Arrtemp1);
                    }

                    filterResults.values = Arrtemp;
                    filterResults.count = Arrtemp.size();



                } else {

                    ArrayList<CDoAn> Arrtemp1 = new ArrayList<>();
                    ArrayList<CDoAn> Arrtemp = new ArrayList<>();

                    if(filterTrangThai.length() != 0){
                        for (CDoAn _da : arrayListFillter) {
                            if ( _da.getTrangThaiString().toLowerCase().contains(filterTrangThai.toLowerCase())) {
                                Arrtemp1.add(_da);
                            }
                        }
                    }else{
                        Arrtemp1.addAll(arrayListFillter);
                    }
                    if(filterNganh.length() != 0){
                        for (CDoAn _da : Arrtemp1) {
                            if ( _da.getChuyeNganh().toLowerCase().contains(filterNganh.toLowerCase())) {
                                Arrtemp.add(_da);
                            }
                        }
                    }else {
                        Arrtemp.addAll(Arrtemp1);
                    }


                    String valueFilter = charSequence.toString().toLowerCase();
                    ArrayList<CDoAn> Arrtemp2 = new ArrayList<>();
                    for (CDoAn _da : Arrtemp) {
                        if ( _da.getTenDA().toLowerCase().contains(valueFilter)
                                ||_da.getChuyeNganh().toLowerCase().contains(valueFilter)
                                || _da.getGiaoVien().getTenGV().toLowerCase().contains(valueFilter))
                        {
                            Arrtemp2.add(_da);
                        }
                    }
                    filterResults.values = Arrtemp2;
                    filterResults.count = Arrtemp2.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayList = (ArrayList<CDoAn>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



    public  class DAViewHoder extends RecyclerView.ViewHolder{
        TextView txt_Ten,txt_Nganh,txt_gv,txt_trangthai;
        CardView container;
        public DAViewHoder(View itemView) {
            super(itemView);
           // txt_Title = itemView.findViewById(R.id.txt_Title);
            container = itemView.findViewById(R.id.container);
            txt_Ten = itemView.findViewById(R.id.txt_Ten);
            txt_Nganh = itemView.findViewById(R.id.txt_Nganh);
            txt_gv = itemView.findViewById(R.id.txt_gv);
            txt_trangthai = itemView.findViewById(R.id.txt_trangthai);

        }
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{
        TextView txt_Title;
        LinearLayout ln_Diem,ln_Sua,ln_Xoa;
        public MenuViewHolder(View view){
            super(view);
            txt_Title = view.findViewById(R.id.txt_Title);
            ln_Diem = view.findViewById(R.id.ln_Diem);
            ln_Sua = view.findViewById(R.id.ln_Sua);
            ln_Xoa = view.findViewById(R.id.ln_Xoa);

        }
    }
}
