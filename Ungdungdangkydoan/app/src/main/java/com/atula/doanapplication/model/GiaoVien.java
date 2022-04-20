package com.atula.doanapplication.model;

public class GiaoVien {
    String maGV;
    String tenGV;
    String email;

    String tenChuyenNganh;

    public GiaoVien(String maGV, String tenGV, String email,String tenChuyenNganh) {
        this.maGV = maGV;
        this.tenGV = tenGV;
        this.tenChuyenNganh = tenChuyenNganh;
        this.email = email;
    }
    public GiaoVien(GiaoVien gv) {
        this.maGV = gv.getMaGV();
        this.tenGV = gv.getTenGV();
        this.tenChuyenNganh = gv.getTenChuyenNganh();
        this.email = gv.email;
    }


    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public String getTenGV() {
        return tenGV;
    }

    public void setTenGV(String tenGV) {
        this.tenGV = tenGV;
    }

    public String getTenChuyenNganh() {
        return tenChuyenNganh;
    }

    public void setTenChuyenNganh(String tenChuyenNganh) {
        this.tenChuyenNganh = tenChuyenNganh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GiaoVien() {
    }

    @Override
    public String toString() {
        return "GiaoVien{" +
                "maGV='" + maGV + '\'' +
                ", tenGV='" + tenGV + '\'' +
                ", email='" + email + '\'' +
                ", tenChuyenNganh='" + tenChuyenNganh + '\'' +
                '}';
    }

    public String showInfo(){
        return  "Họ và tên : "+ tenGV + "\n"+"Email: "+ email;
    }
}
