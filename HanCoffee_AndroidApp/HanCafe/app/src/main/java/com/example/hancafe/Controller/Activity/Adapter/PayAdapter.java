package com.example.hancafe.Controller.Activity.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hancafe.Model.CartItem;
import com.example.hancafe.Model.Promotion;
import com.example.hancafe.R;


import java.util.List;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.ViewHolder> {
    private List<CartItem> data;
    private List<Promotion> promotions;


    public PayAdapter(List<CartItem> data, List<Promotion> promotions) {
        this.data = data;
        this.promotions = promotions;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_item_pay,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = data.get(position);
        Glide.with(holder.itemView.getContext())
                .load(cartItem.getProductImg())
                .into(holder.imgProduct);

        holder.tvNameProduct.setText(cartItem.getProductName());
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

        int price = cartItem.getQuantity() * cartItem.getProductPrice();
        if (cartItem.getSizeId() == 0) {
            int originalPrice = price;
            holder.tvSize.setText("S");
            holder.tvPriceProduct.setText(String.valueOf(originalPrice));
        } else if (cartItem.getSizeId() == 1) {
            int originalPrice = (int) (price * 1.25);
            holder.tvSize.setText("M");
            holder.tvPriceProduct.setText(String.valueOf(originalPrice));
        } else {
            int originalPrice = (int) (price * 1.5);
            holder.tvSize.setText("L");
            holder.tvPriceProduct.setText(String.valueOf(originalPrice));
        }
    }
    public int calculateTotalPriceAfterDiscount(String promotionCode) {
        int totalPrice = 0;
        for (CartItem cartItem : data) {
            int price = cartItem.getQuantity() * cartItem.getProductPrice();

            if(cartItem.getSizeId() == 1){
                price *= 1.25;
            } else if (cartItem.getSizeId() == 2) {
                price *= 1.5;
            }
            if (!promotionCode.equals("Chưa chọn mã")) {
                // Áp dụng mã khuyến mãi nếu có
                for (Promotion promotion : promotions) {
                    if (promotion.getName().equals(promotionCode)) {
                        if (promotion.getDiscount().endsWith("%")) {
                            double discountPercentage = Double.parseDouble(promotion.getDiscount().substring(0, promotion.getDiscount().length() - 1));
                            price *= (1 - discountPercentage / 100);
                        } else if (promotion.getDiscount().endsWith("đ")) {
                            double discountAmount = Double.parseDouble(promotion.getDiscount().substring(0, promotion.getDiscount().length() - 1));
                            price -= discountAmount;
                        }
                        break;
                    }
                }
            }
            totalPrice += price;
        }
        return totalPrice;
    }

    public int calculateTotalPrice() {
        int totalPrice = 0;
        for (CartItem cartItem : data) {
            totalPrice += cartItem.getQuantity() *cartItem.getProductPrice();
        }
        return totalPrice;
    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private Button btnDelFromCart;
        TextView tvNameProduct, tvPriceProduct;
        TextView tvQuantity, tvSize;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvSize = itemView.findViewById(R.id.tvSize);
        }
    }
}
