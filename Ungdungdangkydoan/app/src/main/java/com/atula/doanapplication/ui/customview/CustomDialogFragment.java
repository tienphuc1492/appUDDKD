package com.atula.doanapplication.ui.customview;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.atula.doanapplication.Interface.IChooseSpecialized;
import com.atula.doanapplication.R;

public class CustomDialogFragment extends DialogFragment {
    IChooseSpecialized input;
    public void setInput(IChooseSpecialized input) {
        this.input = input;
    }

    public CustomDialogFragment(){
    }

    RadioGroup radioGroup;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        dialog.setContentView(R.layout.custom_choose_specialized);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        radioGroup = dialog.findViewById(R.id.radioGroup);


        dialog.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId =  radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) dialog.findViewById(selectedId);
                String value = radioButton.getText().toString();
                input.specialized(value);
                dismiss();
            }
        });

        return dialog;
    }
}
