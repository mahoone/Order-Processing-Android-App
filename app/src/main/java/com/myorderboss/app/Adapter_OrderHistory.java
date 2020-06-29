package com.myorderboss.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_OrderHistory extends RecyclerView.Adapter<Adapter_OrderHistory.MyViewHolder>{

    private ArrayList<OrderCompleted> list;
    private Context context;

    public Adapter_OrderHistory(ArrayList<OrderCompleted> list, Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.cust_name.setText(list.get(position).getCust_name());
        holder.order_total.setText(list.get(position).getTotal_cost());
        holder.order_date.setText(list.get(position).getOrder_date());
        holder.order_material_cost.setText(list.get(position).getCost_material());
        holder.order_room_l.setText(list.get(position).getRoomL());
        holder.order_room_w.setText(list.get(position).getRoomW());
        holder.order_room_area.setText(list.get(position).getRoom_area());
        holder.order_vat.setText(list.get(position).getVat());
        holder.order_set_cost.setText(list.get(position).getSet_cost());
        holder.order_user_id.setText(list.get(position).getUser_id());
        holder.order_status.setText(list.get(position).getOrder_active());
        switch(list.get(position).getCost_material()){
            case "Carpet":
                holder.imageView.setImageResource(R.drawable.ic_carpettwo);
                break;
            case "Wood":
                holder.imageView.setImageResource(R.drawable.ic_parquet);
                break;
            case "Tiles":
                holder.imageView.setImageResource(R.drawable.ic_layers_black_24dp);
                break;
            case "Laminate":
                holder.imageView.setImageResource(R.drawable.ic_carpettwo);
                break;
            case "Vinyl":
                holder.imageView.setImageResource(R.drawable.ic_carpettwo);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return  list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cust_name, order_total, order_date, order_material_cost, order_room_l, order_room_w,
                order_room_area, order_vat, order_set_cost, order_user_id, order_status;
        CircleImageView imageView;
        private String key;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cust_name = itemView.findViewById(R.id.tv_customer_name);
            order_total = itemView.findViewById(R.id.tv_order_cost);
            order_date = itemView.findViewById(R.id.tv_date_completed);
            order_material_cost = itemView.findViewById(R.id.tv_cost_material);
            order_room_l = itemView.findViewById(R.id.tv_room_l);
            order_room_w = itemView.findViewById(R.id.tv_room_w);
            order_room_area = itemView.findViewById(R.id.tv_room_area);
            order_vat = itemView.findViewById(R.id.tv_vat);
            order_set_cost = itemView.findViewById(R.id.tv_set_cost);
            order_user_id = itemView.findViewById(R.id.tv_user_id);
            order_status = itemView.findViewById(R.id.tv_order_active);
            imageView = itemView.findViewById(R.id.img_order);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, CompletedOrderDetails.class);

                    intent.putExtra("cust_name", cust_name.getText().toString());
                    intent.putExtra("order_date", order_date.getText().toString());
                    intent.putExtra("total_cost", order_total.getText().toString());
                    intent.putExtra("cost_material", order_material_cost.getText().toString());
                    intent.putExtra("vat", order_vat.getText().toString());
                    intent.putExtra("roomW", order_room_w.getText().toString());
                    intent.putExtra("roomL", order_room_l.getText().toString());
                    intent.putExtra("set_cost", order_set_cost.getText().toString());
                    intent.putExtra("room_area", order_room_area.getText().toString());
                    intent.putExtra("order_active", order_status.getText().toString());
                    intent.putExtra("cust_id", order_user_id.getText().toString());

                    context.startActivity(intent);
                }
            });
        }
    }
}
