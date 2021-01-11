package com.border.launcher.iconcolor;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.border.launcher.R;
import com.flask.colorpicker.ColorPickerView;

public class QSBColorActivity extends Dialog implements View.OnClickListener {

    ColorPickerView colorPicker;
    TextView titleName;

    public QSBColorActivity(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // define based features for an alert Dialog
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.color_picker_dialog);

        Button applyButton = findViewById(R.id.dialog_color_apply_button);
        applyButton.setOnClickListener(this);

        Button cancelButton = findViewById(R.id.dialog_color_cancel_button);
        cancelButton.setOnClickListener(this);

        colorPicker = findViewById(R.id.color_picker_view);
        colorPicker.setColor(Color.parseColor("#FFFFFF"), true);

        titleName = findViewById(R.id.color_picker_title);
        titleName.setText("Select Search Bar Color");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_color_apply_button:

                SharedPreferences prefs = getContext().getSharedPreferences(getContext().getString
                        (R.string.preference_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(getContext().getString(R.string.pref_qsb_color),
                        colorPicker.getSelectedColor());

                editor.apply();


            default:
                dismiss();

        }
    }

}
