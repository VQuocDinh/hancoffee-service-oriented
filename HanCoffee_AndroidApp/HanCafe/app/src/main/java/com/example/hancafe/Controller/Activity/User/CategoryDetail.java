package com.example.hancafe.Controller.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Controller.Activity.Adapter.ProductsAdapter;
import com.example.hancafe.Controller.Api.ApiResponse;
import com.example.hancafe.Controller.Api.ApiService;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.Model.Product;
import com.example.hancafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDetail extends AppCompatActivity implements ProductsAdapter.OnItemClickListener {
    private RecyclerView rvProduct;
    private ProductsAdapter productsAdapter;
    private ImageView btnBack;
    private TextView tvCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        setControl();
        setEvent();
    }
    private void setEvent() {
        LinearLayoutManager linearLayoutManagerProduct = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvProduct.setLayoutManager(linearLayoutManagerProduct);

        Intent intent = getIntent();
        CategoryProduct category = (CategoryProduct) intent.getSerializableExtra("category");
        String categoryId = category.getId();
        tvCategoryName.setText(category.getName());

        ApiService apiService = ApiService.apiService;
        Call<ApiResponse<List<Product>>> call = apiService.getProductList();
        call.enqueue(new Callback<ApiResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Product>>> call, Response<ApiResponse<List<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = new ArrayList<>();
                    for (Product product: response.body().getData()){
                        if(product.getIdCategory().equals(categoryId)){
                            if(product.getStatus() != 1){
                                products.add(product);
                            }
                        }

                    }
                    // Xử lý danh sách sản phẩm ở đây
                    productsAdapter = new ProductsAdapter(products);
                    rvProduct.setAdapter(productsAdapter);
                    productsAdapter.setOnItemProductClickListener(CategoryDetail.this);
                } else {
                    // Xử lý khi có lỗi xảy ra
                }
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Product>>> call, Throwable throwable) {

            }
        });

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("Products");
//
//        Query query = myRef.orderByChild("idCategory").equalTo(categoryId);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        String productId = dataSnapshot.child("id").getValue(String.class);
//                        int productStatus = dataSnapshot.child("status").getValue(int.class);
//                        if (productStatus == 0) {
//                            String productName = dataSnapshot.child("name").getValue(String.class);
//                            String productImg = dataSnapshot.child("purl").getValue(String.class);
//                            int productPrice = dataSnapshot.child("price").getValue(Integer.class);
//                            String productDecs = dataSnapshot.child("describe").getValue(String.class);
//                            String productIdCategory = dataSnapshot.child("idCategory").getValue(String.class);
//                            int producQuantity = dataSnapshot.child("quantity").getValue(Integer.class);
//
//                            Product product = new Product(productImg, productName, productDecs, productId, productStatus, productPrice, productIdCategory, producQuantity);
//                            products.add(product);
//                        }
//
//                    }
//                    productsAdapter = new ProductsAdapter(products);
//                    rvProduct.setAdapter(productsAdapter);
//
//                    productsAdapter.setOnItemProductClickListener(CategoryDetail.this);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setControl() {
        rvProduct = findViewById(R.id.rvProduct);
        btnBack = findViewById(R.id.btnBack);
        tvCategoryName = findViewById(R.id.tvCategoryName);
    }

    @Override
    protected void onResume(){
        super.onResume();
//        fetchProduct();
    }

//    private void fetchProduct(){
//        data_pd.clear();
//        data_pd.addAll(productHelper.fetchProduct());
//        productsAdapter.notifyDataSetChanged();
//    }

    @Override
    public void onItemProductClick(int position) {
        List<Product> productList = productsAdapter.getData();
        Product product = productList.get(position);
        Intent intent = new Intent(this,ProductDetail.class);
        intent.putExtra("product", (Serializable) product);
        startActivity(intent);
    }
}