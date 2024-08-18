package com.example.hancafe.Controller.Activity.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hancafe.Controller.Activity.Admin.MainAdminActivity;
import com.example.hancafe.Controller.Activity.Admin.Product.EditProductFragment;
import com.example.hancafe.Controller.Api.ApiResponse;
import com.example.hancafe.Controller.Api.ApiService;
import com.example.hancafe.Model.Product;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retrofit2.Call;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.myViewHolder> {

    private List<Product> productList;
    private OnItemClickListener mListener;
    private Context mContext;

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ProductAdapter(Context context, List<Product> products) {
        mContext = context;
        productList = products;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_product_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Product model = productList.get(position);

        holder.name.setText(model.getName());
        holder.price.setText(String.valueOf(model.getPrice()));
        holder.describe.setText(model.getDescribe());
        holder.quantity.setText(String.valueOf(model.getQuantity()));
        Glide.with(holder.itemView.getContext())
                .load(model.getPurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("product", model);

                EditProductFragment editProductFragment = new EditProductFragment();
                editProductFragment.setArguments(bundle);

                ((MainAdminActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_admin, editProductFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Hiển thị hộp thoại xác nhận xóa
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("Bạn có chắc muốn xóa sản phẩm này?");
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ApiService apiService = ApiService.apiService;
                            Call<ApiResponse<Product>> call = apiService.deleteProduct(model.getId(), 1);

                            call.enqueue(new retrofit2.Callback<ApiResponse<Product>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<Product>> call, retrofit2.Response<ApiResponse<Product>> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        productList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, productList.size());
                                        Toast.makeText(mContext, "Đã xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(mContext, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                                    Toast.makeText(mContext, "Lỗi khi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Người dùng không muốn xóa, đóng dialog
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && mListener != null) {
                    mListener.onItemClick(productList.get(position)); // Gọi phương thức onItemClick
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(productList != null){
            return productList.size();
        }
        return 0;
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name,price,describe, quantity;

        Button btnEdit,btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img1);
            name = (TextView) itemView.findViewById(R.id.nametext);
            price = (TextView) itemView.findViewById(R.id.pricetext);
            describe = (TextView) itemView.findViewById(R.id.describetext);
            quantity = itemView.findViewById(R.id.tvQuantity);

            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);


        }
    }
}

