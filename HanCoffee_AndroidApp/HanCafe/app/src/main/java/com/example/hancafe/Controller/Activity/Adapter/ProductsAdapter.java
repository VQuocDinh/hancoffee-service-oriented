package com.example.hancafe.Controller.Activity.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hancafe.Model.Product;
import com.example.hancafe.R;


import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder>{
    private List<Product> data;
    private OnItemClickListener listener;
    public ProductsAdapter(List<Product> data) {
        this.data = data;
    }
    public List<Product> getData() {
        return data;
    }
    public void setData(List<Product> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_productlist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = data.get(position);
        Glide.with(holder.itemView.getContext())
                .load(product.getPurl())
                .into(holder.imgProduct);

        holder.tvNameProduct.setText(product.getName());
        holder.tvPriceProduct.setText(String.valueOf(product.getPrice()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemProductClick(position); // Gọi phương thức onItemClick
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameProduct,tvPriceProduct;
        ImageView imgProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
            imgProduct =itemView.findViewById(R.id.imgProduct);
        }
    }
    public interface OnItemClickListener {
        void onItemProductClick(int position);
    }
    public void setOnItemProductClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
