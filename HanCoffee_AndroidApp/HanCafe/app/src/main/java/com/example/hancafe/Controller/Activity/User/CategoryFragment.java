package com.example.hancafe.Controller.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Controller.Activity.Adapter.CategoryMainAdapter;
import com.example.hancafe.Controller.Api.ApiResponse;
import com.example.hancafe.Controller.Api.ApiService;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment implements CategoryMainAdapter.OnItemClickListener {
    RecyclerView rvCategory;
    List<CategoryProduct> categories;
    CategoryMainAdapter categoryAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);



        rvCategory = view.findViewById(R.id.rvCategory);
        initCategory();
        return view;
    }

    private void initCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCategory.setLayoutManager(linearLayoutManager);
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
                    categoryAdapter = new CategoryMainAdapter(categoryProducts);
                    rvCategory.setAdapter(categoryAdapter);
                    categoryAdapter.setOnItemCategoryClickListener(CategoryFragment.this);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CategoryProduct>>> call, Throwable throwable) {

            }
        });
    }

    @Override
    public void onItemCategoryClick(int position) {
        List<CategoryProduct> categoryList = categoryAdapter.getCatData();
        CategoryProduct category = categoryList.get(position);
        Intent intent = new Intent(getActivity(), CategoryDetail.class);
        intent.putExtra("category", (Serializable) category);
        startActivity(intent);
    }
}