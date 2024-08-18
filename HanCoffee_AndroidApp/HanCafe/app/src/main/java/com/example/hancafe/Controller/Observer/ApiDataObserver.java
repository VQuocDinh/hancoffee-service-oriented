package com.example.hancafe.Controller.Observer;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hancafe.Controller.Activity.User.HomeFragment;
import com.example.hancafe.Controller.Api.ApiResponse;
import com.example.hancafe.Controller.Api.ApiService;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiDataObserver {

    private final HomeFragment homeFragment;

    public ApiDataObserver(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    public void startListening() {
        callApiCategoryProduct();
        callApiProduct();

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference productsRef = database.getReference("Products");
//        DatabaseReference categoriesRef = database.getReference("Category_Products");
//
//        productsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Product> products = new ArrayList<>();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    String productId = dataSnapshot.child("id").getValue(String.class);
//                    int productStatus = dataSnapshot.child("status").getValue(int.class);
//                    if (productStatus == 0) {
//                        String productName = dataSnapshot.child("name").getValue(String.class);
//                        String productImg = dataSnapshot.child("purl").getValue(String.class);
//                        int productPrice = dataSnapshot.child("price").getValue(Integer.class);
//                        String productDecs = dataSnapshot.child("describe").getValue(String.class);
//                        String productIdCategory = dataSnapshot.child("idCategory").getValue(String.class);
//                        int productQuantity = dataSnapshot.child("quantity").getValue(Integer.class);
//
//                        Product product = new Product(productImg, productName, productDecs, productId, productStatus, productPrice, productIdCategory, productQuantity);
//                        products.add(product);
//                    }
//                }
//                homeFragment.updateProducts(products);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//            }
//        });
//
//        categoriesRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<CategoryProduct> categories = new ArrayList<>();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    String catId = dataSnapshot.child("id").getValue(String.class);
//                    String catName = dataSnapshot.child("name").getValue(String.class);
//                    String catImg = dataSnapshot.child("curl").getValue(String.class);
//                    int status = dataSnapshot.child("status").getValue(Integer.class);
//                    CategoryProduct category = new CategoryProduct(catId, catName, catImg, status);
//
//                    if (category != null && category.getStatus() != 1) {
//                        categories.add(category);
//                    }
//                }
//                homeFragment.updateCategories(categories);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//            }
//        });
    }

    private void callApiProduct() {
        ApiService apiService = ApiService.apiService;
        Call<ApiResponse<List<Product>>> call = apiService.getProductList();
        call.enqueue(new Callback<ApiResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Product>>> call, Response<ApiResponse<List<Product>>> response) {
                if(response.isSuccessful()){
                    List<Product> products = new ArrayList<>();
                    for (Product product: response.body().getData()){
                        if(product.getStatus() != 1){
                            products.add(product);
                        }
                    }
                    homeFragment.updateProducts(products);
                } else {
                    Toast.makeText(homeFragment.requireContext(), "Lỗi khi gọi API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Product>>> call, Throwable throwable) {

            }
        });
    }

    private void callApiCategoryProduct() {
        ApiService apiService = ApiService.apiService;
        Call<ApiResponse<List<CategoryProduct>>> call = apiService.getCategoryList();
        call.enqueue(new Callback<ApiResponse<List<CategoryProduct>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CategoryProduct>>> call, Response<ApiResponse<List<CategoryProduct>>> response) {
                if(response.isSuccessful()){
                    List<CategoryProduct> categoryProducts = new ArrayList<>();
                    for (CategoryProduct categoryProduct: response.body().getData()){
                        if(categoryProduct.getStatus() != 1){
                            categoryProducts.add(categoryProduct);
                        }
                    }
                    homeFragment.updateCategories(categoryProducts);
                } else {
                    Toast.makeText(homeFragment.requireContext(), "Lỗi khi gọi API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CategoryProduct>>> call, Throwable throwable) {

            }
        });
    }
}

