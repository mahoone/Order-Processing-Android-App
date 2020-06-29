package com.myorderboss.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class CompletedOrderDetails extends AppCompatActivity {

    //Receiving data from object

    private String key;
    private String cust_name;
    private String order_date;
    private String total_cost;
    private String cost_material;
    private String roomW;
    private String roomL;
    private String room_area;
    private String vat;
    private String set_cost;
    private String order_active;
    private String cust_id;

    private FirebaseAuth firebaseAuth;

    private TextView comp_cust_name, comp_total_cost, comp_vat, comp_cost_material, comp_set_cost, comp_room_area, comp_room_w, comp_room_l, comp_order_date;


    private Button logout;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_order_details);

        firebaseAuth = FirebaseAuth.getInstance();
        logout = (Button)findViewById(R.id.logoutMenu);

        // Action bar

        setPropertiesActionBar();

        // Get data from Adapter

        cust_name = getIntent().getStringExtra("cust_name");
        order_date = getIntent().getStringExtra("order_date");
        total_cost = getIntent().getStringExtra("total_cost");
        cost_material = getIntent().getStringExtra("cost_material");
        vat = getIntent().getStringExtra("vat");
        roomW = getIntent().getStringExtra("roomW");
        roomL = getIntent().getStringExtra("roomL");
        set_cost = getIntent().getStringExtra("set_cost");
        room_area = getIntent().getStringExtra("room_area");
        order_active = getIntent().getStringExtra("order_active");
        cust_id = getIntent().getStringExtra("cust_id");

        // ... and set that data to TextView's

        comp_cust_name = (TextView) findViewById(R.id.cust_name);
        comp_cust_name.setText(cust_name);
        comp_order_date = (TextView)findViewById(R.id.order_date);
        comp_order_date.setText(order_date);
        comp_total_cost = (TextView)findViewById(R.id.total_cost);
        comp_total_cost.setText(total_cost);
        comp_vat = (TextView)findViewById(R.id.vat);
        comp_vat.setText(vat);
        comp_set_cost = (TextView)findViewById(R.id.set_cost);
        comp_set_cost.setText(set_cost);
        comp_room_area = (TextView)findViewById(R.id.room_area);
        comp_room_area.setText(room_area);
        comp_room_w = (TextView)findViewById(R.id.room_w);
        comp_room_w.setText(roomW);
        comp_room_l = (TextView)findViewById(R.id.room_l);
        comp_room_l.setText(roomL);
        comp_cost_material = (TextView)findViewById(R.id.material);
        comp_cost_material.setText(cost_material);
        comp_set_cost = (TextView)findViewById(R.id.material_cost);
        comp_set_cost.setText(set_cost);
    }

    // Action bar properties

    private void setPropertiesActionBar(){
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_completed_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
