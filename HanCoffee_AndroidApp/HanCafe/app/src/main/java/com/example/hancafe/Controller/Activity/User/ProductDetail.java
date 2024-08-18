package com.example.hancafe.Controller.Activity.User;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.example.hancafe.Controller.Api.ApiResponse;
import com.example.hancafe.Controller.Api.ApiService;
import com.example.hancafe.Model.CartDetail;
import com.example.hancafe.Model.Product;
import com.example.hancafe.Model.User;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetail extends AppCompatActivity {
    private TextView tvQuantity, tvNameProduct, tvProductInfor, tvCartCount, tvPrice;
    private Button btn1, btn2, btn3, btnDecrease, btnIncrease, btnAddCart, btnSmallSize, btnMidSize, btnBigSize;
    private ImageView btnBack, btnCart, imgProduct;
    private int count = 1;
    private int cartItemCount = 0;
    private Boolean btnSmallSizeIsActive, btnMidSizeIsActive, btnBigSizeIsActive;
    private int idSize = 0;
    private int originalPrice; // Biến lưu trữ giá gốc của sản phẩm
    private int currentPrice; // biến lưu giá hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setControl();
        initCartCount();
        setEvent();
    }

    private void initCartCount() {

//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//
//        if (currentUser != null) {
//            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("CartDetail");
//            cartRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    cartItemCount = 0;
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        CartDetail cartDetail = dataSnapshot.getValue(CartDetail.class);
//                        if (cartDetail != null && cartDetail.getIdCart().equals(currentUser.getUid())) {
//                            cartItemCount += cartDetail.getQuantity();
//                        }
//                    }
//                    tvCartCount.setText(String.valueOf(cartItemCount));
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    // Handle possible errors.
//                }
//            });
//        }
    }

    private void setControl() {
        btn1 = findViewById(R.id.btnSmallSize);
        btn2 = findViewById(R.id.btnMidSize);
        btn3 = findViewById(R.id.btnBigSize);
        btnBack = findViewById(R.id.btnBack);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnAddCart = findViewById(R.id.btnAddCart);
        btnSmallSize = findViewById(R.id.btnSmallSize);
        btnMidSize = findViewById(R.id.btnMidSize);
        btnBigSize = findViewById(R.id.btnBigSize);
        btnCart = findViewById(R.id.btnCart);
        imgProduct = findViewById(R.id.imgProduct);
        tvNameProduct = findViewById(R.id.tvNameProduct);
        tvProductInfor = findViewById(R.id.tvProductInfor);
        tvCartCount = findViewById(R.id.tvCartCount);
        tvPrice = findViewById(R.id.tvPrice);
        tvPrice.setVisibility(View.GONE);

    }

    private void setEvent() {
        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("product");

        Glide.with(this)
                .load(product.getPurl())
                .into(imgProduct);
        tvNameProduct.setText(product.getName());
        tvProductInfor.setText(product.getDescribe());

        originalPrice = product.getPrice();
        DecimalFormat df = new DecimalFormat("###,###.##");
        String formattedPrice = df.format(originalPrice) + "đ";
        tvPrice.setText(formattedPrice);
//        tvPrice.setText(String.valueOf(originalPrice));

        currentPrice = originalPrice;


        btn1.setBackgroundColor(getResources().getColor(R.color.black));
        btn2.setBackgroundColor(getResources().getColor(R.color.mainColor));
        btn3.setBackgroundColor(getResources().getColor(R.color.mainColor));
        btnDecrease.setBackgroundColor(getResources().getColor(R.color.transparent));
        btnIncrease.setBackgroundColor(getResources().getColor(R.color.transparent));
        btnAddCart.setBackgroundColor(getResources().getColor(R.color.mainColor));

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1) {
                    count--;
                    tvQuantity.setText(String.valueOf(count));
                    updatePrice(currentPrice, count);
                }
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                tvQuantity.setText(String.valueOf(count));
                updatePrice(currentPrice, count);
            }
        });


        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToCart();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetail.this, MainActivity.class);
                startActivity(intent);
            }
        });


        btnSmallSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonState((Button) v);
                idSize = 0;
                currentPrice = originalPrice;
                updatePrice(currentPrice, count); // Hiển thị giá gốc cho kích thước nhỏ
            }
        });

        btnMidSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMidSize.setBackgroundColor(getResources().getColor(R.color.black));
                updateButtonState((Button) v);
                idSize = 1;
                currentPrice = (int) (originalPrice * 1.25);
                updatePrice(currentPrice, count);
            }
        });
        btnBigSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBigSize.setBackgroundColor(getResources().getColor(R.color.black));
                updateButtonState((Button) v);
                idSize = 2;
                currentPrice = (int) (originalPrice * 1.5);
                updatePrice(currentPrice, count);
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetail.this, MainActivity.class);
                intent.putExtra("openFragment", "cart");
                startActivity(intent);

            }
        });
    }

    private void updatePrice(int newPrice, int quantity) {
        int totalPrice = newPrice * quantity;
        DecimalFormat df = new DecimalFormat("###,###.##");
        String formattedPrice = df.format(totalPrice) + "đ";
        tvPrice.setText(formattedPrice);
//        tvPrice.setText(String.valueOf(totalPrice));
    }

    private void addProductToCart() {
        Intent intent = getIntent();

        Product product = (Product) intent.getSerializableExtra("product");
        if (product == null) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = "6667c5ede23cbcb3793bb6ec";

        ApiService apiService = ApiService.apiService;
        Call<ApiResponse<User>> call = apiService.addToCart(userId, product.getId());
        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful()) {
                    // Xử lý phản hồi từ API thành công
                    System.out.println("product " + response.body().getData());
                    ApiResponse<User> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        User user = apiResponse.getData();
//                        System.out.println("user" + user.getCartData().getSize());
//                        // Cập nhật giao diện người dùng để hiển thị giỏ hàng mới
//                        // Ví dụ: cập nhật số lượng sản phẩm trong giỏ hàng trên giao diện
//                        int cartItemCount = user.getCartData().size();
//                        tvCartCount.setText(String.valueOf(cartItemCount));

                        // Hiển thị thông báo thành công
                        Toast.makeText(ProductDetail.this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    } else {
                        // Xử lý khi API trả về không thành công
                        String message = apiResponse.getMessage();
                        Toast.makeText(ProductDetail.this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý phản hồi từ API thất bại
                    Toast.makeText(ProductDetail.this, "Lỗi: Không thể thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                // Xử lý lỗi khi gọi API
                Toast.makeText(ProductDetail.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products").child(product.getId());
//        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Product fetchedProduct = snapshot.getValue(Product.class);
//                if (fetchedProduct != null) {
//                    if (fetchedProduct.getQuantity() > 0) {
//                        // Tiếp tục thêm sản phẩm vào giỏ hàng
//                        int quantityToBuy = Integer.parseInt(tvQuantity.getText().toString());
//                        processAddToCart(fetchedProduct, quantityToBuy);
//                    } else {
//                        // Hiển thị thông báo hết hàng
//                        showAlert(ProductDetail.this, "Thông báo", "Sản phẩm đã hết hàng.");
//                    }
//                } else {
//                    Toast.makeText(ProductDetail.this, "Lỗi: Không tìm thấy thông tin sản phẩm", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(ProductDetail.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });




    }

    private void processAddToCart(Product product, int quantityToBuy) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("CartDetail");
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products").child(product.getId());
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int maxIdCartItem = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int idCartItem = dataSnapshot.child("idCartItem").getValue(Integer.class);
                    if (idCartItem > maxIdCartItem) {
                        maxIdCartItem = idCartItem;
                    }
                }
                int newIdCartItem = maxIdCartItem + 1;
                String idCart = currentUser.getUid();
                String idProduct = product.getId();
                int quantity = Integer.parseInt(tvQuantity.getText().toString());

                int newQuantity = product.getQuantity() - quantityToBuy;
                if (newQuantity < 0) {
                    showAlert(ProductDetail.this, "Thông báo", "Sản phẩm bạn đặt vượt quá số lượng tồn kho.");
                } else {
                    CartDetail newItem = new CartDetail(newIdCartItem, idCart, idProduct, idSize, quantity);
                    cartRef.push().setValue(newItem)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    productRef.child("quantity").setValue(newQuantity)
                                            .addOnSuccessListener(aVoid -> showAlert(ProductDetail.this, "Thông báo", "Sản phẩm đã được thêm vào giỏ hàng"))
                                            .addOnFailureListener(e -> Toast.makeText(ProductDetail.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProductDetail.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateButtonState(Button clickedButton) {
        btnSmallSizeIsActive = clickedButton == btnSmallSize;
        btnMidSizeIsActive = clickedButton == btnMidSize;
        btnBigSizeIsActive = clickedButton == btnBigSize;

        btnSmallSize.setBackgroundColor(btnSmallSizeIsActive ? getResources().getColor(R.color.black) : getResources().getColor(R.color.mainColor));
        btnMidSize.setBackgroundColor(btnMidSizeIsActive ? getResources().getColor(R.color.black) : getResources().getColor(R.color.mainColor));
        btnBigSize.setBackgroundColor(btnBigSizeIsActive ? getResources().getColor(R.color.black) : getResources().getColor(R.color.mainColor));
    }

    public static void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}