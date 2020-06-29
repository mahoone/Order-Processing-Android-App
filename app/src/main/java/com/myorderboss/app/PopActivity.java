package com.myorderboss.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PopActivity extends Activity {

    private TextView customerName;
    private TextView material;
    private TextView orderDate;
    private TextView roomSize;
    private TextView totalCost;
    private Button done;
    private Dialog dialog;

    //Receiving data from object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);
        declareUIs();

        // Popup

        dialog = new Dialog(this);

        // Get Data from NewOrderActivity

        Intent intent = getIntent();
        String customer_name = intent.getStringExtra(NewOrderActivity.EXTRA_TEXT);
        String total_cost = intent.getStringExtra(NewOrderActivity.EXTRA_TEXT_2);
        String materialUpdate = intent.getStringExtra(NewOrderActivity.EXTRA_TEXT_3);
        String orderDueDate = intent.getStringExtra(NewOrderActivity.EXTRA_TEXT_4);
        String roomSizeUpdate = intent.getStringExtra(NewOrderActivity.EXTRA_TEXT_5);

        customerName.setText(customer_name);
        material.setText(materialUpdate);
        totalCost.setText(total_cost);
        orderDate.setText(orderDueDate);
        roomSize.setText(roomSizeUpdate);

        // Parameters for pop up window

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopActivity.this, SecondActivity.class));
            }
        });
    }

    // Declare UIs

    private void declareUIs(){
        customerName = (TextView)findViewById(R.id.tvCustomerName);
        material = (TextView)findViewById(R.id.tvMaterial);
        orderDate = (TextView)findViewById(R.id.tvOrderDueDate);
        roomSize = (TextView)findViewById(R.id.tvRoomSize);
        totalCost = (TextView)findViewById(R.id.tvTotalCost);
        done = (Button) findViewById(R.id.btnDone);
    }
}
