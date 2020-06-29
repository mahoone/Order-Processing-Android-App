package com.myorderboss.app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText mCustName, mRoomL, mRoomW, mDueDate, mMaterialCost;
    private TextView mRoomArea, tvSubTotal, tvTotal, tvVat, tvOrderStatus;
    private Button mUpdate, mDelete, mCompleted;
    private Double L, W, A, M, area, sub, vat, total;
    private String TotalConvert, VATConvert, SubConvert, AreaConvert;
    private RadioGroup radioGroup;
    private RadioButton radioButton, addMaterial;
    private SwitchCompat switchVat;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    private Dialog dialog;
    OrderCompleted order_completed;
    private static final String TAG = "OrderDetailsActivity";

    //Receiving data from object

    private String key;
    private String cust_name;
    private String order_date;
    private String total_cost;
    private String cost_material;
    private String roomW;
    private String roomL;
    private String room_area;
    private String vat_view;
    private String set_cost;
    private String order_status;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // Validate if '.' is entered just once

        commaValidation();

        key = getIntent().getStringExtra("key");
        cust_name = getIntent().getStringExtra("cust_name");
        order_date = getIntent().getStringExtra("order_date");
        total_cost = getIntent().getStringExtra("total_cost");
        cost_material = getIntent().getStringExtra("cost_material");
        vat_view = getIntent().getStringExtra("vat");
        roomW = getIntent().getStringExtra("roomW");
        roomL = getIntent().getStringExtra("roomL");
        set_cost = getIntent().getStringExtra("set_cost");
        room_area = getIntent().getStringExtra("room_area");
        order_status = getIntent().getStringExtra("order_active");

        mCustName = (EditText) findViewById(R.id.etCustName);
        mCustName.setText(cust_name);
        mDueDate = (EditText) findViewById(R.id.etOrderDueDate);
        mDueDate.setText(order_date);
        mMaterialCost = (EditText) findViewById(R.id.etMaterialCost);
        mMaterialCost.setText(set_cost);
        mRoomArea = (TextView) findViewById(R.id.tvTotalRoomArea);
        mRoomArea.setText(room_area);
        tvSubTotal = (TextView) findViewById(R.id.tvSubTotal);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvTotal.setText(total_cost);
        tvVat = (TextView) findViewById(R.id.tvVat);
        tvVat.setText(vat_view);
        mRoomL = (EditText) findViewById(R.id.etRoomL);
        mRoomL.setText(roomL);
        mRoomW = (EditText) findViewById(R.id.etRoomW);
        mRoomW.setText(roomW);
        tvOrderStatus = (TextView) findViewById(R.id.tvOrderStatus);
        tvOrderStatus.setText(order_status);

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mUpdate = (Button) findViewById(R.id.btnUpdateOrder);
        mDelete = (Button) findViewById(R.id.btnDelete);
        switchVat = (SwitchCompat) findViewById(R.id.switchVat);
        mCompleted = (Button) findViewById(R.id.btnCompleted);

        mAuth = FirebaseAuth.getInstance();

        dialog = new Dialog(this);

        ref = FirebaseDatabase.getInstance().getReference("order_completed");

        // Mark order as completed

        mCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(OrderDetailsActivity.this);
                alert.setTitle("Complete order");
                alert.setMessage("Are you sure you want to complete this order?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        // Temporary if statement to exclude 'Add' radiobutton.

                        if (radioGroup.getCheckedRadioButtonId() != -1 && !radioButton.getText().toString().equals("Add"))
                        {
                            getValues();
                            ref.push().setValue(order_completed);
                            Toast.makeText(OrderDetailsActivity.this, "Order completed!", Toast.LENGTH_SHORT).show();

                            new FirebaseDatabaseHelper().deleteOrder(key, new FirebaseDatabaseHelper.DataStatus() {
                                @Override
                                public void DataIsLoaded(List<Order> orders, List<String> keys) {

                                }

                                @Override
                                public void DataIsInserted() {

                                }

                                @Override
                                public void DataIsUpdated() {

                                }

                                @Override
                                public void DataIsDeleted() {
                                    startActivity(new Intent(OrderDetailsActivity.this, SecondActivity.class));
                                    finish();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(OrderDetailsActivity.this, "Select type of material", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });

        // Check if order contains VAT

        checkIfVat();

        // Update textviews as the user types the values in

        //updateTxt();

        // Date picker

        mDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Switch VAT

        switchVatHintColor();

        // Action bar

        setPropertiesActionBar();

        // Save Order and calculate total cost of order

        mUpdate.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(OrderDetailsActivity.this, "Select type of material", Toast.LENGTH_SHORT).show();
                    }
                }}
        });

        // Delete order with yes/no dialog

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(OrderDetailsActivity.this);
                alert.setTitle("Delete order");
                alert.setMessage("Are you sure you want to delete this order?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        new FirebaseDatabaseHelper().deleteOrder(key, new FirebaseDatabaseHelper.DataStatus() {
                            @Override
                            public void DataIsLoaded(List<Order> orders, List<String> keys) {

                            }

                            @Override
                            public void DataIsInserted() {

                            }

                            @Override
                            public void DataIsUpdated() {

                            }

                            @Override
                            public void DataIsDeleted() {
                                startActivity(new Intent(OrderDetailsActivity.this, SecondActivity.class));
                                finish();
                            }
                        });
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
            }
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
        order.setCost_material(radioButton.getText().toString());
        order.setSet_cost(mMaterialCost.getText().toString());
        order.setUser_id(mAuth.getCurrentUser().getUid());
        order.setOrder_active("true");


        new FirebaseDatabaseHelper().updateOrder(key, order, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Order> orders, List<String> keys) {

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {
                Toast.makeText(OrderDetailsActivity.this, "Order updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(OrderDetailsActivity.this, SecondActivity.class));
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
        order.setCost_material(radioButton.getText().toString());
        order.setSet_cost(mMaterialCost.getText().toString());
        order.setUser_id(mAuth.getCurrentUser().getUid());
        order.setOrder_active("true");


        new FirebaseDatabaseHelper().updateOrder(key, order, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Order> orders, List<String> keys) {

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {
                Toast.makeText(OrderDetailsActivity.this, "Order updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(OrderDetailsActivity.this, SecondActivity.class));
            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }

    /* New popup activity

    public void openPopActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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
        }, 1500);
    }*/

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

    public void checkRadioButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = (RadioButton)findViewById(radioId);

        /*if (addMaterial.isChecked()) {
            Toast.makeText(this, "This feature is available for premium version only.", Toast.LENGTH_SHORT).show();
        }*/
    }


    // Action bar properties

    private void setPropertiesActionBar(){
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_editorder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Live Update textviews after values has been entered by user

//    private void updateTxt() {
//        mRoomW.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if ((s.length() > 0) && (!mRoomL.getText().toString().isEmpty())) {
//                    //mRoomArea.setText("");
//                    // Calculate Length and Width of Room
//                    double result = (Double.parseDouble(mRoomL.getText().toString())) *
//                            (Double.parseDouble(mRoomW.getText().toString()));
//                    mRoomArea.setText(String.valueOf(result) + "m" + "\u00B2");
//                } else {
//                    mRoomArea.setText("0");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }

    // Check if order contains VAT

    private void checkIfVat(){
        if (tvVat.getText().toString().equals("N/A")){
            switchVat.setChecked(false);
        }else{
            switchVat.setChecked(true);
            switchVat.setHintTextColor(Color.BLACK);
        }
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

    // Comma validation

    private void commaValidation() {

    }

    // Get values from text boxes

    private void getValues(){
        order_completed = new OrderCompleted();
        order_completed.setRoomL(mRoomL.getText().toString());
        order_completed.setRoomW(mRoomW.getText().toString());
        order_completed.setCust_name(mCustName.getText().toString());
        order_completed.setRoom_area(mRoomArea.getText().toString());
        order_completed.setOrder_date(mDueDate.getText().toString());
        order_completed.setVat(tvVat.getText().toString());
        order_completed.setTotal_cost(tvTotal.getText().toString());
        order_completed.setCost_material(radioButton.getText().toString());
        order_completed.setSet_cost(mMaterialCost.getText().toString());
        order_completed.setUser_id(mAuth.getCurrentUser().getUid());
        order_completed.setOrder_active("false");
    }
}

