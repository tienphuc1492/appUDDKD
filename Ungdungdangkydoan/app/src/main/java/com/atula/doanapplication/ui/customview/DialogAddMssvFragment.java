package com.atula.doanapplication.ui.customview;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.atula.doanapplication.Interface.CallBackDialogNotify;
import com.atula.doanapplication.Interface.IChooseSpecialized;
import com.atula.doanapplication.R;
import com.google.android.material.textfield.TextInputEditText;

public class DialogAddMssvFragment extends DialogFragment {
    IChooseSpecialized callBack;
    public void setInput(IChooseSpecialized callBack) {
        this.callBack = callBack;
    }

    public DialogAddMssvFragment(){

    }
    TextInputEditText input_MSSV;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            Dialog dialog = new Dialog(getActivity(), R.style.DialogTheme);


        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        dialog.setContentView(R.layout.custom_dialog_notify);

       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        input_MSSV = dialog.findViewById(R.id.input_MSSV);

        dialog.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_MSSV.getText().toString().length() != 0){
                    callBack.specialized(input_MSSV.getText().toString());
                }

                dismiss();
            }
        });
        dialog.findViewById(R.id.Yess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        return dialog;
    }
}
