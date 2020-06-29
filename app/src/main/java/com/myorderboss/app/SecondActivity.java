package com.myorderboss.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button logout;
    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private FloatingActionButton neworder;
    private AdView mAdView;
    //public RecyclerView_Config.OrdersAdapter mOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Google adds

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Action bar

        getSupportActionBar().setElevation(0); //remove underline actionbar
        getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        firebaseAuth = FirebaseAuth.getInstance();

        logout = (Button)findViewById(R.id.logoutMenu);
        neworder = (FloatingActionButton) findViewById(R.id.fab);

        neworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, NewOrderActivity.class));
            }
        });

        // Recycler view

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_orders);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);

        new FirebaseDatabaseHelper().readOrders(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Order> orders, List<String> keys) {
                findViewById(R.id.loading_orders).setVisibility(View.GONE);
                new RecyclerView_Config().setConfig(mRecyclerView, SecondActivity.this, orders, keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }

    // Logout function

    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(SecondActivity.this, MainActivity.class));
    }

    // Inflate layout with menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        getMenuInflater().inflate(R.menu.menu, menu);

        // Search functionality -NOT WORKING- Adapter is empty.
        // Try to initialize Adapter inside THIS activity instead of calling it from
        // Different class (RecyclerView_Config).

        /*MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newtext) {
                mOrderAdapter.getFilter().filter(newtext);
                return true;
            }
        });*/

        if(user!=null){
            menu.getItem(0).setVisible(true); // New order
            menu.getItem(1).setVisible(true); // Logout
        }else{
            menu.getItem(0).setVisible(false); // New order
            menu.getItem(1).setVisible(false); // Logout
        }
        return true;
    }

    // Menu item - Logout and New Order

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        progressDialog = new ProgressDialog(this);

        switch(item.getItemId()){
            case R.id.new_order: {
                startActivity(new Intent(this, NewOrderActivity.class));
                return true;
            }
            case R.id.order_history: {
                startActivity(new Intent(this, OrderHistory.class));
                return true;
            }
            case R.id.logoutMenu: {
                progressDialog.setMessage("Logging out");
                progressDialog.show();
                Logout();
                invalidateOptionsMenu();
            }
        }
        return true;
    }

    // Animation

    private void runLayoutAnimation (RecyclerView mRecyclerView){
        Context context = mRecyclerView.getContext();

        LayoutAnimationController layoutAnimationController =
                AnimationUtils.loadLayoutAnimation(context,R.anim.layout_item_animation);

        mRecyclerView.setLayoutAnimation(layoutAnimationController);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mRecyclerView.scheduleLayoutAnimation();
    }
}
