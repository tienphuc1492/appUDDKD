package com.atula.doanapplication.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CUser {
    String mssv;
    String ten;
    String ngaySinh;
    String chuyenNganh;
    String sDT;
    String nhomTruong = "";
    String mK;
    String maDoAn = "";


    public String getNhomTruong() {
        return nhomTruong;
    }

    public void setNhomTruong(String nhomTruong) {
        this.nhomTruong = nhomTruong;
    }



    public String getMaDoAn() {
        return maDoAn;
    }

    public void setMaDoAn(String maDoAn) {
        this.maDoAn = maDoAn;
    }

    public CUser() {
    }

    public CUser(String mssv, String ten, String ngaySinh, String chuyenNganh, String sDT) {
        this.mssv = mssv;
        this.ten = ten;
        this.ngaySinh = ngaySinh;
        this.chuyenNganh = chuyenNganh;
        this.sDT = sDT;
        this.mK = "";

    }

    public CUser(String mssv, String ten, String ngaySinh, String chuyenNganh, String sDT, String mK) {
        this.mssv = mssv;
        this.ten = ten;
        this.ngaySinh = ngaySinh;
        this.chuyenNganh = chuyenNganh;
        this.sDT = sDT;
        this.mK = mK;

        //dsThanhVien = new ArrayList<>();
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getChuyenNganh() {
        return chuyenNganh;
    }

    public void setChuyenNganh(String chuyenNganh) {
        this.chuyenNganh = chuyenNganh;
    }

    public String getsDT() {
        return sDT;
    }

    public void setsDT(String sDT) {
        this.sDT = sDT;
    }

    public String getmK() {
        return mK;
    }

    public void setmK(String mK) {
        this.mK = mK;
    }


    public boolean checkIsNhomTruong(){
        if(nhomTruong.length() == 0){
            return true;
        }else{
            if(nhomTruong.equals(mssv)){
                return  true;
            }else{
                return  false;
            }
        }
    }
}
