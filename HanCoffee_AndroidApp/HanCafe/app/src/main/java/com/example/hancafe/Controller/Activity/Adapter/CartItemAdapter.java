package com.example.hancafe.Controller.Activity.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hancafe.Model.CartItem;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

    private List<CartItem> data;
    private List<CartItem> selectedList = new ArrayList<>();
    private ProductsAdapter.OnItemClickListener listener;
    public CartItemAdapter(List<CartItem> data) {
        this.data = data;
    }
    public List<CartItem> getSelectedList() {
        return selectedList;
    }
    public void setData(List<CartItem> data) {
        this.data = data;
    }
    private int totalPrice = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_item_cartdetail, parent, false);
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

        holder.btnDelFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Bạn có chắc muốn xóa sản phẩm khỏi giỏ hàng ?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int idCartItem = cartItem.getIdCartItem();
                                DatabaseReference cartItemsRef = FirebaseDatabase.getInstance().getReference("CartDetail");
                                cartItemsRef.orderByChild("idCartItem").equalTo(idCartItem).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            dataSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    // Cập nhật số lượng sản phẩm trong kho
                                                    DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products").child(cartItem.getProductId());
                                                    productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                int currentQuantity = snapshot.child("quantity").getValue(Integer.class);
                                                                int newQuantity = currentQuantity + cartItem.getQuantity();
                                                                productRef.child("quantity").setValue(newQuantity).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        int position = holder.getAdapterPosition();
                                                                        if (position != RecyclerView.NO_POSITION) {
                                                                            data.remove(position);
                                                                        }
                                                                        notifyDataSetChanged();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        // Xử lý lỗi nếu cập nhật số lượng sản phẩm thất bại
                                                                        Toast.makeText(v.getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            // Xử lý lỗi nếu truy vấn sản phẩm thất bại
                                                            Toast.makeText(v.getContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemProductClick(position); // Gọi phương thức onItemClick
                }
//                if (listener != null) {
//                    listener.onItemProductClick(position);
//                }
            }
        });
        holder.cbProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selectedList.add(cartItem);
                    calculateTotalPrice();
                } else {
                    selectedList.remove(cartItem);

                }
            }
        });


    }


    private boolean isSelectAllCheckbox(CompoundButton buttonView) {
        return buttonView.getId() == R.id.cbSelectAll;
    }
    public int calculateTotalPrice() {

        for (CartItem cartItem : data) {
            totalPrice += cartItem.getQuantity() * cartItem.getProductPrice();
        }
        return totalPrice;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<CartItem> getData() {
        return data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private ImageButton btnDelFromCart;
        private TextView tvNameProduct, tvPriceProduct, tvQuantity, tvSize;
        private CheckBox cbProduct;
//        private LinearLayout lnCart, lnCartEmpty;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvSize = itemView.findViewById(R.id.tvSize);
            cbProduct = itemView.findViewById(R.id.cbProduct);
            btnDelFromCart = itemView.findViewById(R.id.btnDelFromCart);
//            lnCart = itemView.findViewById(R.id.lnCart);
//            lnCartEmpty = itemView.findViewById(R.id.lnCartEmpty);
        }
    }

}
