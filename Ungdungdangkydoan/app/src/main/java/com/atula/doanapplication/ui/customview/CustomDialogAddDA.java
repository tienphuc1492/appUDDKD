package com.atula.doanapplication.ui.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.atula.doanapplication.Interface.CallBackDialogNotify;
import com.atula.doanapplication.Interface.IChooseFilter;
import com.atula.doanapplication.Interface.IChooseSpecialized;
import com.atula.doanapplication.R;
import com.google.android.material.textfield.TextInputEditText;

public class CustomDialogAddDA  {
    public void showDialog(Activity activity,IChooseSpecialized input){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_add_da);

        EditText input_tenDA = (EditText) dialog.findViewById(R.id.input_tenDA);
        dialog.findViewById(R.id.txt_tieptuc).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            input.specialized(input_tenDA.getText().toString());
            dialog.dismiss();
        }
        });
        dialog.findViewById(R.id.txt_Huy).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss(); }
        });

        dialog.show();

    }

    public void showDialogChonNganh(Activity activity,IChooseSpecialized input){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_choose_specialized);

        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        dialog.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId =  radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) dialog.findViewById(selectedId);
                String value = radioButton.getText().toString();
                input.specialized(value);
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    public void showDialogFilter(Activity activity,int id1, int id2, IChooseFilter input){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_filter);

        RadioGroup radioGroup1 = dialog.findViewById(R.id.group1);
        RadioGroup radioGroup2 = dialog.findViewById(R.id.group2);

        RadioButton rd1 = dialog.findViewById(R.id.rd1);
        RadioButton rd2 = dialog.findViewById(R.id.rd2);
        RadioButton rd3 = dialog.findViewById(R.id.rd3);
        RadioButton rd4 = dialog.findViewById(R.id.rd4);
        RadioButton rd5 = dialog.findViewById(R.id.rd5);
        RadioButton rd6 = dialog.findViewById(R.id.rd6);
        RadioButton rd7 = dialog.findViewById(R.id.rd7);
        RadioButton rd8 = dialog.findViewById(R.id.rd8);

        switch (id1){
            case R.id.rd1:
                rd1.setChecked(true);
                break;
            case R.id.rd2:
                rd2.setChecked(true);
                break;
            default:
                rd3.setChecked(true);
        }
        switch (id2){
            case R.id.rd4:
                rd4.setChecked(true);
                break;
            case R.id.rd5:
                rd5.setChecked(true);
                break;
            case R.id.rd6:
                rd6.setChecked(true);
                break;
            case R.id.rd7:
                rd7.setChecked(true);
                break;
            default:
                rd8.setChecked(true);
        }



        dialog.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId =  radioGroup1.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) dialog.findViewById(selectedId);
                String value1 = radioButton.getText().toString();


                int selectedId2 =  radioGroup2.getCheckedRadioButtonId();
                RadioButton radioButton2 = (RadioButton) dialog.findViewById(selectedId2);
                String value2 = radioButton2.getText().toString();

                input.specialized(value1+","+value2, selectedId,selectedId2);
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    public void chamDiem(Activity activity,IChooseSpecialized input){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_chamdiem);

        EditText input_tenDA = (EditText) dialog.findViewById(R.id.input_diem);
        dialog.findViewById(R.id.txt_tieptuc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.specialized(input_tenDA.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.txt_Huy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); }
        });

        dialog.show();

    }
}
