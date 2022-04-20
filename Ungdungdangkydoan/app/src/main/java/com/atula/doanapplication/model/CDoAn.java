package com.atula.doanapplication.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CDoAn implements Serializable,Comparable<CDoAn>{
    private boolean showMenu = false;

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }
    String key ;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String tenDA;
    int trangThai;
    String mucTieuDetai;
    String moiTruong;
    String thoiGian;
    String diem;
    String chuyeNganh;
    GiaoVien giaoVien;
    ArrayList<CUser> listUser;

    public CDoAn(String tenDA, String mucTieuDetai, String moiTruong, String thoiGian, String chuyeNganh, GiaoVien giaoVien) {
        this.tenDA = tenDA;
        this.mucTieuDetai = mucTieuDetai;
        this.moiTruong = moiTruong;
        this.thoiGian = thoiGian;
        this.chuyeNganh = chuyeNganh;
        this.giaoVien = giaoVien;
        this.trangThai = 0;
        this.diem = "";
    }

    public CDoAn() {
    }

    public CDoAn(String tenDA, int trangThai, String mucTieuDetai, String moiTruong, String thoiGian, String diem, String chuyeNganh, GiaoVien giaoVien) {
        this.tenDA = tenDA;
        this.trangThai = trangThai;
        this.mucTieuDetai = mucTieuDetai;
        this.moiTruong = moiTruong;
        this.thoiGian = thoiGian;
        this.diem = diem;
        this.chuyeNganh = chuyeNganh;
        this.giaoVien = giaoVien;
    }

    public ArrayList<CUser> getListUser() {
        return listUser;
    }

    public void setListUser(ArrayList<CUser> listUser) {
        this.listUser = listUser;
    }

    public String getChuyeNganh() {
        return chuyeNganh;
    }

    public void setChuyeNganh(String chuyeNganh) {
        this.chuyeNganh = chuyeNganh;
    }

    public GiaoVien getGiaoVien() {
        return giaoVien;
    }

    public void setGiaoVien(GiaoVien giaoVien) {
        this.giaoVien = giaoVien;
    }

    public String getDiem() {
        return diem;
    }

    public void setDiem(String diem) {
        this.diem = diem;
    }

    public String getTenDA() {
        return tenDA;
    }

    public void setTenDA(String tenDA) {
        this.tenDA = tenDA;
    }


    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
    public String getTrangThaiString(){
        return trangThai == 0 ? "Chưa đăng kí" : "Đã được đăng kí";
    }


    public String getMucTieuDetai() {
        return mucTieuDetai;
    }

    public void setMucTieuDetai(String mucTieuDetai) {
        this.mucTieuDetai = mucTieuDetai;
    }

    public String getMoiTruong() {
        return moiTruong;
    }

    public void setMoiTruong(String moiTruong) {
        this.moiTruong = moiTruong;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public CDoAn(boolean showMenu, String key, String tenDA, int trangThai, String mucTieuDetai, String moiTruong, String thoiGian, String diem, String chuyeNganh, GiaoVien giaoVien) {
        this.showMenu = showMenu;
        this.key = key;
        this.tenDA = tenDA;
        this.trangThai = trangThai;
        this.mucTieuDetai = mucTieuDetai;
        this.moiTruong = moiTruong;
        this.thoiGian = thoiGian;
        this.diem = diem;
        this.chuyeNganh = chuyeNganh;
        this.giaoVien = giaoVien;
    }

    @Override
    public int compareTo(@NonNull CDoAn contactsModel) {
        return tenDA.compareToIgnoreCase(contactsModel.tenDA);
    }
}
