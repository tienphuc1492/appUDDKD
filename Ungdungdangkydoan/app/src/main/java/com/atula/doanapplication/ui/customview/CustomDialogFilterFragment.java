package com.atula.doanapplication.ui.customview;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.atula.doanapplication.Interface.IChooseFilter;
import com.atula.doanapplication.Interface.IChooseSpecialized;
import com.atula.doanapplication.R;

public class CustomDialogFilterFragment extends DialogFragment {
    IChooseFilter input;
    int id1;
    int id2;
    public void setInput(IChooseFilter input) {
        this.input = input;
    }

    public CustomDialogFilterFragment(int id1,int id2){
        this.id1 = id1;
        this.id2 = id2;
    }
    RadioGroup radioGroup1;
    RadioGroup radioGroup2;
    RadioButton rd1 ;
    RadioButton rd2 ;
    RadioButton rd3 ;
    RadioButton rd4;
    RadioButton rd5;
    RadioButton rd6 ;
    RadioButton rd7 ;
    RadioButton rd8;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        dialog.setContentView(R.layout.custom_dialog_filter);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        radioGroup1 = dialog.findViewById(R.id.group1);
        radioGroup2 = dialog.findViewById(R.id.group2);
         rd1 = dialog.findViewById(R.id.rd1);
         rd2 = dialog.findViewById(R.id.rd2);
         rd3 = dialog.findViewById(R.id.rd3);
         rd4 = dialog.findViewById(R.id.rd4);
         rd5 = dialog.findViewById(R.id.rd5);
         rd6 = dialog.findViewById(R.id.rd6);
         rd7 = dialog.findViewById(R.id.rd7);
         rd8 = dialog.findViewById(R.id.rd8);

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
                input.specialized(value1+","+value2,selectedId,selectedId2);
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
