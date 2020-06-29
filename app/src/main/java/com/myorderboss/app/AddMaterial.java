package com.myorderboss.app;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class AddMaterial extends Activity {

    private TextInputLayout textInputName;
    private TextInputLayout textInputPrice;
    private TextView exitPopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_material_popup);

        // Set PopUp settings

        popUpSettings();

        // Set UI's

        setviews();

        // Exit

        exitPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setviews(){
        textInputName = findViewById(R.id.til_material_name);
        textInputPrice = findViewById(R.id.til_material_price);
        exitPopUp = findViewById(R.id.tv_exit_popup);
    }

    private boolean validateInput() {
        String nameInput = textInputName.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            textInputName.setError("Please enter name of material");
            return false;
        }else if (nameInput.length() > 8) {
            textInputName.setError("Name too long");
            return false;
        }else {
            textInputName.setError(null);
            return true;
        }
    }

    private boolean validatePriceInput() {
        String priceInput = textInputPrice.getEditText().getText().toString().trim();

        if (priceInput.isEmpty()) {
            textInputPrice.setError("Please enter price of material");
            return false;
        }else if (priceInput.length() > 4) {
            textInputPrice.setError("It can't be that expensive?!");
            return false;
        }else {
            textInputPrice.setError(null);
            return true;
        }
    }

    public void confirmInput(View v) {
        if (!validateInput() | !validatePriceInput()) {
            return;
        }
    }

    public void popUpSettings() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.8));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }
}
