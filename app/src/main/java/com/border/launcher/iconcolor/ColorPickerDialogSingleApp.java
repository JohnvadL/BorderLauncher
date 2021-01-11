package com.border.launcher.iconcolor;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.border.launcher.R;
import com.flask.colorpicker.ColorPickerView;

public class ColorPickerDialogSingleApp extends Dialog implements View.OnClickListener {

    ColorPickerView colorPicker;
    TextView titleName;

    protected ColorPickerDialogSingleApp(@NonNull Context context) {
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
        titleName.setText(IconActivity.label.toUpperCase());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_color_apply_button:
                IconActivity.newColor = colorPicker.getSelectedColor();
                dismiss();
            default:
                dismiss();

        }
    }

}
