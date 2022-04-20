package com.atula.doanapplication.config;

import android.util.Log;

import androidx.annotation.NonNull;

import com.atula.doanapplication.Interface.CallBackFirebase;
import com.atula.doanapplication.model.CDoAn;
import com.atula.doanapplication.model.CUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DBFirebaseHelper {
    private static DBFirebaseHelper instance = null;
    public static DBFirebaseHelper getInstance()
    {
        if (instance == null)
        {
            instance = new DBFirebaseHelper();
        }
        return instance;
    }

    public DBFirebaseHelper() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        table_User = firebaseDatabase.getReference(TABLE_USER);
        table_GroupUser = firebaseDatabase.getReference(TABLE_GROUP);
        table_DoAn = firebaseDatabase.getReference(TABLE_DOAN);
    }

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference table_User;
    private DatabaseReference table_GroupUser;
    private DatabaseReference table_DoAn;

    public static  String TABLE_USER = "user_table";
    public static  String TABLE_GROUP = "group_table";
    public static  String TABLE_DOAN = "doan_table";

    public void createUser(CUser user, CallBackFirebase callBack){
        DatabaseReference referenceCheck = table_User.child(user.getMssv());
        final boolean[] isCallBack = {false};
        referenceCheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                if(isCallBack[0] == false){
                    isCallBack[0] = true;

                    if(snapshot.getChildrenCount() == 0){
                        callBack.Successfull("Đăng kí thành công !");
                        table_User.child(user.getMssv()).setValue(user);

                    }else{
                        callBack.Error("Sinh viên này đã đăng kí !");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callBack.Error("Vui lòng thử lại sau !");
            }
        });

    }

    public void login(String mssv,String pass, CallBackFirebase callBack){
        DatabaseReference referenceCheck = table_User.child(mssv);
        final boolean[] isCallBack = {false};
        referenceCheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {

                if(isCallBack[0] == false){
                    isCallBack[0] = true;
                    if(snapshot.getValue() == null){
                        callBack.Error("Tài khoản chưa được đăng kí ");
                    }else{

                        CUser cUser = snapshot.getValue(CUser.class);

                        if(pass.equals(cUser.getmK())){
                            InfoUser infoUser = InfoUser.getInstance();
                            infoUser.setcUser(cUser);
                            callBack.Successfull("Đăng nhập thành công !");

                        }else {
                            callBack.Error("Sai mật khẩu. Vui lòng thử lại !");
                        }

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callBack.Error(error.getMessage());
            }
        });
    }



    public void createDoAn(CDoAn _doAn, ValueEventListener valueEventListener){
        String _KEY = table_DoAn.push().getKey();
        _doAn.setKey(_KEY);

        table_DoAn.addValueEventListener(valueEventListener);
        table_DoAn.child(_KEY).setValue(_doAn);
    }
    public void removeDoAn(String keyDoAn){
        table_DoAn.child(keyDoAn).setValue(null);
    }
    public void updateDoAn(CDoAn _doAn){
        table_DoAn.child(_doAn.getKey()).setValue(_doAn);
    }
    public void selectDoAn(ValueEventListener valueEventListener){
        table_DoAn.addValueEventListener(valueEventListener);
    }
}
