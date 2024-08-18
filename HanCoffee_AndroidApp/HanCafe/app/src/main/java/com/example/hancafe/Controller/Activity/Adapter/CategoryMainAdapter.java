package com.example.hancafe.Controller.Activity.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.R;


import java.util.List;

public class CategoryMainAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<CategoryProduct> catData;
    private OnItemClickListener listener;

    public CategoryMainAdapter(List<CategoryProduct> catData) {
        this.catData = catData;
    }

    public List<CategoryProduct> getCatData() {
        return catData;
    }


    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category_main, parent, false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryProduct category = catData.get(position);

        holder.tvCategoryName.setText(category.getName());
        Glide.with(holder.itemView.getContext())
                .load(category.getCurl())
                .into(holder.catImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemCategoryClick(position); // Gọi phương thức onItemClick
                }
//                if (listener != null) {
//                    listener.onItemCategoryClick(position);
//                }
            }
        });
    }



    @Override
    public int getItemCount() {
        if(catData != null){
            return catData.size();
        }
        return 0;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView catImg;
        LinearLayout mainLayout;
        TextView tvCategoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catImg = itemView.findViewById(R.id.catImg);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }

    public interface OnItemClickListener {
        void onItemCategoryClick(int position);
    }

    public void setOnItemCategoryClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
