package com.example.hancafe.Controller.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Model.Promotion;
import com.example.hancafe.R;

import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder> {
    private final List<Promotion> promotions;
    private Context mContext;

    public PromotionAdapter(Context context, List<Promotion> promotions) {
        this.promotions = promotions;
        this.mContext = context;
    }

    public class PromotionViewHolder extends RecyclerView.ViewHolder {
        public TextView promotionName, promotionDiscount, promotionDescription, promotionDuration;

        public PromotionViewHolder(View view) {
            super(view);
            promotionName = view.findViewById(R.id.text_promotion_name);
            promotionDiscount = view.findViewById(R.id.text_promotion_discount);
            promotionDescription = view.findViewById(R.id.text_promotion_description);
            promotionDuration = view.findViewById(R.id.text_promotion_duration);
        }
    }

    @NonNull
    @Override
    public PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promotion_item_layout, parent, false);
        return new PromotionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionViewHolder holder, int position) {
        Promotion promotion = promotions.get(position);
        holder.promotionName.setText(promotion.getName());
        holder.promotionDiscount.setText("Mức khuyến mãi: " + promotion.getDiscount());
        holder.promotionDescription.setText("Mô tả: " + promotion.getDescription());
        holder.promotionDuration.setText("Thời gian: " + promotion.getStartTime() + " - " + promotion.getEndTime());
    }

    @Override
    public int getItemCount() {
        return promotions.size();
    }
}
