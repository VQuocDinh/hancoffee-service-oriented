package com.example.hancafe.Controller.Activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hancafe.Controller.Activity.User.Bill;

import com.example.hancafe.Model.OrderManagement;
import com.example.hancafe.Model.OrderDetail;
import com.example.hancafe.R;

import java.util.ArrayList;
import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder> {
    private List<OrderManagement> orderManagements;
    private Context context;


    public OrderStatusAdapter(List<OrderManagement> orderManagements, Context context) {
        this.orderManagements = orderManagements;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderManagement orderManagement = orderManagements.get(position);
        
        holder.tvTotalPrice.setText(String.valueOf(orderManagement.getPrice()));
        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(orderManagement.getOrderDetails());
        holder.chillRecycleView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.chillRecycleView.setAdapter(orderDetailAdapter);

        holder.tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Bill.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("orderManagement", (Parcelable) orderManagement);
                intent.putParcelableArrayListExtra("orderDetails", (ArrayList<? extends Parcelable>) orderManagement.getOrderDetails());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderManagements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTotalPrice, tvDetail;
        RecyclerView chillRecycleView;
        LinearLayout orderLayout;
        LinearLayout lnEmpty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            chillRecycleView = itemView.findViewById(R.id.chillRecycleView);
            orderLayout = itemView.findViewById(R.id.orderLayout);
            tvDetail = itemView.findViewById(R.id.tvDetail);

            lnEmpty = itemView.findViewById(R.id.lnEmpty);
        }
    }

    public static class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ChildViewHolder> {
        private List<OrderDetail> orderDetails;

        public OrderDetailAdapter(List<OrderDetail> orderDetails) {
            this.orderDetails = orderDetails;
        }

        @NonNull
        @Override
        public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order_detail, parent, false);
            return new ChildViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
            OrderDetail orderDetail = orderDetails.get(position);
            holder.tvNameProduct.setText(orderDetail.getNameProduct());
            holder.tvQuantity.setText(String.valueOf(orderDetail.getQuantity()));

            int price = orderDetail.getPriceProduct();
            if (orderDetail.getIdSize() == 0) {
                holder.tvSize.setText("S");
                holder.tvPriceProduct.setText(String.valueOf(price));
            } else if (orderDetail.getIdSize() == 1) {
                holder.tvSize.setText("M");
                holder.tvPriceProduct.setText(String.valueOf(price * 1.25));
            } else {
                holder.tvSize.setText("L");
                holder.tvPriceProduct.setText(String.valueOf(price * 1.5));
            }
            Glide.with(holder.itemView.getContext())
                    .load(orderDetail.getImgProduct())
                    .into(holder.imgProduct);
        }

        @Override
        public int getItemCount() {
            return orderDetails.size();
        }

        public class ChildViewHolder extends RecyclerView.ViewHolder {
            TextView tvNameProduct, tvPriceProduct, tvSize, tvQuantity;
            ImageView imgProduct;

            public ChildViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
                tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
                imgProduct = itemView.findViewById(R.id.imgProduct);
                tvSize = itemView.findViewById(R.id.tvSize);
                tvQuantity = itemView.findViewById(R.id.tvQuantity);

            }
        }
    }
}
