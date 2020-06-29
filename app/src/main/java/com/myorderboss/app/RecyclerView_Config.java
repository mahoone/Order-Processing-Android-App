package com.myorderboss.app;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RecyclerView_Config {
    FirebaseAuth mAuth;
    private static FirebaseUser user;
    private Context mContext;
    public OrdersAdapter mOrderAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<Order> orders, List<String> keys) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mContext = context;
        mOrderAdapter = new OrdersAdapter(orders, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mOrderAdapter);
    }

    class OrderItemView extends RecyclerView.ViewHolder{
        private TextView mCost_material, mRoomW, mRoomL, mSet_Cost, mOrderStatus;
        private TextView mCust_name;
        private TextView mOrder_date;
        private TextView mRoom_area;
        private TextView mTotal_cost;
        private TextView mVat;
        private TextView mRoomTotal;

        private String key;

        public OrderItemView (ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.order_list_item, parent, false));

            mCust_name = (TextView) itemView.findViewById(R.id.tvCustName);
            mTotal_cost = (TextView) itemView.findViewById(R.id.tvTotalCost);
            mOrder_date = (TextView) itemView.findViewById(R.id.tvOrderDate);
            mRoomTotal = (TextView) itemView.findViewById(R.id.tvRoomSizeShow);
            mCost_material = (TextView) itemView.findViewById(R.id.tvMaterial);
            mVat = (TextView) itemView.findViewById(R.id.tvVat);
            mRoomW = (TextView) itemView.findViewById(R.id.tvRoomW);
            mRoomL = (TextView) itemView.findViewById(R.id.tvRoomL);
            mSet_Cost = (TextView) itemView.findViewById(R.id.tvSetCost);
            mOrderStatus = (TextView) itemView.findViewById(R.id.tvOrderStatus);

            // Data for Update/Delete activity --> OrderDetailsActivity

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                    intent.putExtra("key", key);
                    intent.putExtra("cust_name", mCust_name.getText().toString());
                    intent.putExtra("order_date", mOrder_date.getText().toString());
                    intent.putExtra("total_cost", mTotal_cost.getText().toString());
                    intent.putExtra("cost_material", mCost_material.getText().toString());
                    intent.putExtra("vat", mVat.getText().toString());
                    intent.putExtra("roomW", mRoomW.getText().toString());
                    intent.putExtra("roomL", mRoomL.getText().toString());
                    intent.putExtra("set_cost", mSet_Cost.getText().toString());
                    intent.putExtra("room_area", mRoomTotal.getText().toString());
                    intent.putExtra("order_active", mOrderStatus.getText().toString());

                    mContext.startActivity(intent);
                }
            });
        }

        // Bind Data

        public void bind(Order order, String key){
            mCust_name.setText(order.getCust_name());
            mRoomTotal.setText(order.getRoom_area());
            mOrder_date.setText(order.getOrder_date());
            mTotal_cost.setText(order.getTotal_cost());
            mCost_material.setText(order.getCost_material());
            mVat.setText(order.getVat());
            mRoomW.setText(order.getRoomW());
            mRoomL.setText(order.getRoomL());
            mSet_Cost.setText(order.getSet_cost());
            mOrderStatus.setText(order.getOrder_active());

            this.key = key;

            /* Show remaining days to complete a order.
            // Not working properly, does not account for months. Works only in days.

            String orderDate = mOrder_date.getText().toString();
            Calendar calCurr = Calendar.getInstance();
            Calendar day = Calendar.getInstance();
            try {
                day.setTime(new SimpleDateFormat("dd/mm/yyyy").parse(orderDate));
                if(day.before(calCurr)){
                    mDateDaysRemaining.setText("" + (day.get(Calendar.DATE) - (calCurr.get(Calendar.DATE))));
                }else{
                    mDateDaysRemaining.setText("error");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
        }
    }
    public class OrdersAdapter extends RecyclerView.Adapter<OrderItemView> {
        private List<Order> mOrderList;
        private List<String> mKeys;

        public OrdersAdapter(List<Order> mOrderList, List<String> mKeys) {
            this.mOrderList = mOrderList;
            this.mKeys = mKeys;
        }

        @Override
        public OrderItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OrderItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderItemView holder, int position) {
            holder.bind(mOrderList.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mOrderList.size();
        }


        // Search functionality -NOT WORKING- Adapter is empty.
        // Try to initialize Adapter inside THIS activity instead of calling it from
        // Different class (RecyclerView_Config).
        /*
        @Override
        public Filter getFilter() {
            return orderFilter;
        }

        private Filter orderFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Order> filteredList = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0){
                    filteredList.addAll(mOrderListFull);
                }else{
                    String filterPattern = charSequence.toString().toLowerCase().trim();

                    for (Order order : mOrderListFull){
                        if (order.getRoomL().toLowerCase().contains(filterPattern)){
                            filteredList.add(order);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                mOrderListFull.clear();
                mOrderListFull.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };*/
    }
}
