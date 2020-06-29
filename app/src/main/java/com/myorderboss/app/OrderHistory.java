package com.myorderboss.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity {

    private RecyclerView mRecyclerViewOrderHistory;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth firebaseAuth;
    private Button logout;
    private Context context;
    private static final String TAG = "OrderHistory";
    private ImageView imageView;
    DatabaseReference ref;
    ArrayList<OrderCompleted> list = new ArrayList<OrderCompleted>();
    androidx.appcompat.widget.SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Firebase reference

        ref = FirebaseDatabase.getInstance().getReference("order_completed");
        firebaseAuth = FirebaseAuth.getInstance();

        // Declare UIs

        logout = (Button)findViewById(R.id.logoutMenu);
        mRecyclerViewOrderHistory = (RecyclerView)findViewById(R.id.rvOrderHistory);
        searchView = (androidx.appcompat.widget.SearchView) findViewById(R.id.svSearchOrderHistory);
        imageView = (ImageView)findViewById(R.id.img_order);

        // Action bar properties

        getSupportActionBar().setElevation(0); //remove underline actionbar
        getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_order_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar.LayoutParams p = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        p.gravity = Gravity.CENTER;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ref != null)
        {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        list = new ArrayList<>();
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            OrderCompleted order_completed = ds.getValue(OrderCompleted.class);
                            if (order_completed.getUser_id().equals(firebaseAuth.getUid())) {
                                list.add(ds.getValue(OrderCompleted.class));
                            } else{
                            }
                        }
                    }else
                        {
                            Toast.makeText(OrderHistory.this, "No Records", Toast.LENGTH_SHORT).show();
                        }
                    Adapter_OrderHistory adapterOrderHistory = new Adapter_OrderHistory(list, context);
                    mRecyclerViewOrderHistory.setAdapter(adapterOrderHistory);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(OrderHistory.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
        }

        // Search

        if(searchView != null)
        {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    // Logout function

    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(OrderHistory.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        getMenuInflater().inflate(R.menu.menu, menu);
        if(user!=null){
            menu.getItem(0).setVisible(true); // New order
            menu.getItem(1).setVisible(false); // Order history
            menu.getItem(2).setVisible(true); // Logout
        }else{
            menu.getItem(0).setVisible(false); // New order
            menu.getItem(1).setVisible(false); // Order history
            menu.getItem(2).setVisible(false); // Logout
        }
        return true;
    }
    // Menu item - Logout and New Order

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        mProgressDialog = new ProgressDialog(this);

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
                mProgressDialog.setMessage("Logging out");
                mProgressDialog.show();
                Logout();
                invalidateOptionsMenu();
            }
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return true;
    }


    // Set UIs

    private void setUIs(){

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

    // Search

    private void search(String str){
        ArrayList<OrderCompleted> myList = new ArrayList<>();
        for(OrderCompleted object : list)
        {
            if(object.getCust_name().toLowerCase().contains(str.toLowerCase()))
            {
                myList.add(object);
            }
        }
        Adapter_OrderHistory adapterOrderHistory = new Adapter_OrderHistory(myList, context);
        mRecyclerViewOrderHistory.setAdapter(adapterOrderHistory);
    }
}
