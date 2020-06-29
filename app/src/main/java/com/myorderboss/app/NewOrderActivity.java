package com.myorderboss.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.List;

public class NewOrderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static String EXTRA_TEXT = "com.example.directwoodapp.EXTRA_TEST";
    public static String EXTRA_TEXT_2 = "com.example.directwoodapp.EXTRA_TEST_2";
    public static String EXTRA_TEXT_3 = "com.example.directwoodapp.EXTRA_TEST_3";
    public static String EXTRA_TEXT_4 = "com.example.directwoodapp.EXTRA_TEST_4";
    public static String EXTRA_TEXT_5 = "com.example.directwoodapp.EXTRA_TEST_5";

    private EditText mCustName, mRoomL, mRoomW, mDueDate, mMaterialCost;
    private TextView mRoomArea,tvSubTotal, tvTotal, tvVat;
    private Button mSave;
    private Double L, W, A, M, area, sub, vat, total;
    private String TotalConvert, VATConvert, SubConvert, AreaConvert;
    private RadioGroup radioGroup;
    private RadioButton radioButton, addMaterial;
    private LinearLayout check;
    private SwitchCompat switchVat;
    private Dialog dialog;
    private FirebaseAuth mAuth;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        declateUIs();

        // Get the ID of currently logged in user

        mAuth = FirebaseAuth.getInstance();

        // Fake dialog window to get the 'dimming' effect behind pop up window

        dialog = new Dialog(this);

        // Switch VAT

        switchVatHintColor();

        // Update textviews as the user types the values in

        //updateTxt();

        // Validate if '.' is entered just once

        commaValidation();

        // Date picker

        mDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Action bar

        setPropertiesActionBar();

        // Save Order and calculate total cost of order

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validationText()){

                // Calculate Length and Width of Room

                L = Double.parseDouble(mRoomL.getText().toString());
                W = Double.parseDouble(mRoomW.getText().toString());
                M = Double.parseDouble(mMaterialCost.getText().toString());

                A = L * W;

                // Temporary if statement to exclude 'Add' radiobutton.

                if (radioGroup.getCheckedRadioButtonId() != -1 && !radioButton.getText().toString().equals("Add"))
                {
                    if (switchVat.isChecked()) {
                        calculateTotalWithVat();
                    }else{
                        calculateTotal();
                    }
                }
                else
                {
                    Toast.makeText(NewOrderActivity.this, "Select type of material", Toast.LENGTH_SHORT).show();
                }
            }}
        });
    }

    // Set date after selecting date from date picker
    // Add +1 to month because datepicker works on 0-11 months (0 = January etc.)

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month+1) + "/" + year;
        mDueDate.setText(date);
    }

    // Calulcate with VAT

    private void calculateTotalWithVat(){
        // Calculate Subtotal & Convert/Display as String
        sub = A * M;
        SubConvert = String.valueOf(sub);
        tvSubTotal.setText("£" + SubConvert);

        vat = (sub * 20) / 100;
        VATConvert = String.valueOf(vat);
        tvVat.setText("£" + VATConvert);

        total = sub + vat;
        TotalConvert = String.valueOf(total);
        tvTotal.setText("£" + TotalConvert);

        // Convert int values to strings

        AreaConvert = String.valueOf(A);
        mRoomArea.setText(AreaConvert + " m2");

        AreaConvert = String.valueOf(L);
        mRoomL.setText(AreaConvert);

        AreaConvert = String.valueOf(W);
        mRoomW.setText(AreaConvert);

        Order order = new Order();
        order.setRoomL(mRoomL.getText().toString());
        order.setRoomW(mRoomW.getText().toString());
        order.setCust_name(mCustName.getText().toString());
        order.setRoom_area(mRoomArea.getText().toString());
        order.setOrder_date(mDueDate.getText().toString());
        order.setVat(tvVat.getText().toString());
        order.setTotal_cost(tvTotal.getText().toString());
        order.setSet_cost(mMaterialCost.getText().toString());
        order.setCost_material(radioButton.getText().toString());
        order.setUser_id(mAuth.getCurrentUser().getUid());
        order.setOrder_active("true");

        new FirebaseDatabaseHelper().addOrder(order, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Order> orders, List<String> keys) {
            }

            @Override
            public void DataIsInserted() {
                openPopActivity();
            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {
            }
        });
    }

    // Calculate total without VAT

    private void calculateTotal(){
        // Calculate Subtotal & Convert/Display as String
        sub = A * M;

        tvVat.setText("N/A");

        total = sub;
        TotalConvert = String.valueOf(total);
        tvTotal.setText("£" + TotalConvert);

        // Convert int values to strings

        AreaConvert = String.valueOf(A);
        mRoomArea.setText(AreaConvert + " m2");

        AreaConvert = String.valueOf(L);
        mRoomL.setText(AreaConvert);

        AreaConvert = String.valueOf(W);
        mRoomW.setText(AreaConvert);

        Order order = new Order();
        order.setRoomL(mRoomL.getText().toString());
        order.setRoomW(mRoomW.getText().toString());
        order.setCust_name(mCustName.getText().toString());
        order.setRoom_area(mRoomArea.getText().toString());
        order.setOrder_date(mDueDate.getText().toString());
        order.setVat(tvVat.getText().toString());
        order.setTotal_cost(tvTotal.getText().toString());
        order.setSet_cost(mMaterialCost.getText().toString());
        order.setCost_material(radioButton.getText().toString());
        order.setUser_id(mAuth.getCurrentUser().getUid());
        order.setOrder_active("true");


        new FirebaseDatabaseHelper().addOrder(order, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Order> orders, List<String> keys) {

            }

            @Override
            public void DataIsInserted() {
                openPopActivity();
            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {
            }
        });
    }

    // New popup activity

    public void openPopActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //dialog.setContentView(R.layout.activity_pop);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                String custName = mCustName.getText().toString();
                String totalCost = tvTotal.getText().toString();
                String materialUpdate = radioButton.getText().toString();
                String orderDueDate = mDueDate.getText().toString();
                String roomSizeUpdate = mRoomArea.getText().toString();

                Intent intent = new Intent(NewOrderActivity.this, PopActivity.class);
                intent.putExtra(EXTRA_TEXT, custName);
                intent.putExtra(EXTRA_TEXT_2, totalCost);
                intent.putExtra(EXTRA_TEXT_3, materialUpdate);
                intent.putExtra(EXTRA_TEXT_4, orderDueDate);
                intent.putExtra(EXTRA_TEXT_5, roomSizeUpdate);
                startActivity(intent);
            }
        }, 500);
    }

    // Date Picker

    private void showDatePickerDialog(){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                R.style.MyDatePickerDialogTheme,this,year,month,day);

        // Create a TextView programmatically.
        TextView tv = new TextView(this);


        // Create a TextView programmatically
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setPadding(10, 10, 10, 10);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        tv.setText("Schedule");
        tv.setTextSize(15);
        tv.setTextColor(Color.parseColor("#111111"));
        dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dpd.setCustomTitle(tv);

        dpd.show();
    }

    // Textbox validation

    private Boolean validationText(){
        Boolean result = false;

        String name = mCustName.getText().toString();
        String date = mDueDate.getText().toString();
        String roomL = mRoomL.getText().toString();
        String roomW = mRoomW.getText().toString();
        String setCost = mMaterialCost.getText().toString();

        if(name.isEmpty() || date.isEmpty() || roomL.isEmpty() || roomW.isEmpty() || setCost.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }
        return result;
    }


    // Radio Button check

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = (RadioButton)findViewById(radioId);

        if (addMaterial.isChecked()) {
            Toast.makeText(this, "This feature is available for premium version only.", Toast.LENGTH_SHORT).show();
        }
    }


    // Declare UIs

    private void declateUIs(){
        mCustName = (EditText)findViewById(R.id.etCustName);
        mRoomL = (EditText)findViewById(R.id.etRoomL);
        mRoomW = (EditText)findViewById(R.id.etRoomW);
        mDueDate = (EditText)findViewById(R.id.etOrderDueDate);
        mRoomArea = (TextView)findViewById(R.id.tvTotalRoomArea);
        tvSubTotal = (TextView)findViewById(R.id.tvSubTotal);
        tvTotal = (TextView)findViewById(R.id.tvTotal);
        tvVat = (TextView)findViewById(R.id.tvVat);
        mSave = (Button)findViewById(R.id.btnUpdateOrder);
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        addMaterial = (RadioButton) findViewById(R.id.btn_add_material);
        mMaterialCost = (EditText)findViewById(R.id.etMaterialCost);
        check = (LinearLayout)findViewById(R.id.bg_summary);
        switchVat = (SwitchCompat) findViewById(R.id.switchVat);
    }

    // Action bar properties

    private void setPropertiesActionBar(){
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_neworder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Real-time update textviews after values has been entered by user

//    private void updateTxt() {
//        mRoomW.addTextChangedListener(new TextWatcher() {
//
//            String sBackup;
//            double value;
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                try {
//                    if ((s.length() > 0) && (!mRoomL.getText().toString().isEmpty())) {
//                        //mRoomArea.setText("");
//                        // Calculate Length and Width of Room
//                        double result = (Double.parseDouble(mRoomL.getText().toString())) *
//                                (Double.parseDouble(mRoomW.getText().toString()));
//                        mRoomArea.setText(String.valueOf(result) + "m" + "\u00B2");
//                    }
//                } catch (Exception e) {
//                        mRoomArea.setText("0");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                try {
//                    if (editable.toString().equals("") == false) {
//                        value = Double.valueOf(editable.toString().replace(',', '.'));
//                        sBackup = editable.toString();
//                    }
//                } catch (Exception e) {
//                    mRoomW.setText(sBackup);
//                    mRoomW.setSelection(mRoomW.getText().toString().length());
//                }
//            }
//        });
//    }

    // Comma validation

    private void commaValidation() {
        mRoomL.addTextChangedListener(new TextWatcher() {
            String sBackup;

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                double value;
                try {
                    if (editable.toString().equals("") == false) {
                        value = Double.valueOf(editable.toString().replace(',', '.'));
                        sBackup = editable.toString();
                    }
                } catch (Exception e) {
                    mRoomL.setText(sBackup);
                    mRoomL.setSelection(mRoomL.getText().toString().length());
                }
            }
        });
    }

    // Switch VAT

    public void switchVatHintColor(){

        switchVat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    switchVat.setHintTextColor(Color.BLACK);
                }else{
                    switchVat.setHintTextColor(getResources().getColor(R.color.hintcolor));
                }
            }
        });
    }
}
