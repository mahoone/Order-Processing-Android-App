package com.myorderboss.app;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceOrders;
    private DatabaseReference mCurrentUserId;
    private FirebaseAuth mAuth;
    private List<Order> orders = new ArrayList<>();
    private static final String TAG = "DatabaseHelper";

    public interface DataStatus{
        void DataIsLoaded(List<Order> orders, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    //Initialize Database object

    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceOrders = ((FirebaseDatabase) mDatabase).getReference("order");
        mCurrentUserId = ((FirebaseDatabase) mDatabase).getReference("order").child("cust_id");
        mAuth = FirebaseAuth.getInstance();
    }

    public void readOrders(final DataStatus dataStatus){
        mReferenceOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Order order = keyNode.getValue(Order.class);
                    if (order.getUser_id().equals(mAuth.getUid()) && order.getOrder_active().equals("true")) {
                        orders.add(order);
                    }else {
                    }
                }dataStatus.DataIsLoaded(orders,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void addOrder(Order order, final DataStatus dataStatus) {
        String key = mReferenceOrders.push().getKey();
        mReferenceOrders.child(key).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }


    // Update and Delete methods

    public void updateOrder(String key, Order order, final DataStatus dataStatus){
        mReferenceOrders.child(key).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deleteOrder(String key, final DataStatus dataStatus){
        mReferenceOrders.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}
