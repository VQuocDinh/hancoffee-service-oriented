package com.example.hancafe.Controller.Activity.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Controller.Activity.Adapter.CategoryProductAdapter;
import com.example.hancafe.Controller.Activity.Admin.Product.AddCategoryProductFragment;
import com.example.hancafe.Controller.Api.ApiResponse;
import com.example.hancafe.Controller.Api.ApiService;
import com.example.hancafe.Controller.Log.FileLogger;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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


public class CategoryProductAdminFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    List<CategoryProduct> categories = new ArrayList<>();
    CategoryProductAdapter categoryProductAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_category_product, container, false);
        setControl(view);
        initCategory();
        setEvent();
        log();
        return view;
    }

    private void log() {
        // Lấy instance của FileLogger
        FileLogger fileLogger = FileLogger.getFileLogger(getContext());
        // Ghi log
        fileLogger.log("Category Product Admin Fragment created.");
    }

    private void setEvent(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCategoryProductFragment addCategoryProductFragment = new AddCategoryProductFragment();
                // Sử dụng FragmentTransaction để thêm hoặc thay thế Fragment hiện tại bằng Fragment mới
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.container_admin, addCategoryProductFragment); // R.id.fragment_container là ID của Container Fragment trong layout của bạn
                transaction.addToBackStack(null); // Để cho phép người dùng quay lại Fragment trước đó bằng nút Back
                transaction.commit();
            }
        });
    }
    private void initCategory() {
        ApiService apiService = ApiService.apiService;
        Call<ApiResponse<List<CategoryProduct>>> call = apiService.getCategoryList();

        call.enqueue(new Callback<ApiResponse<List<CategoryProduct>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CategoryProduct>>> call, Response<ApiResponse<List<CategoryProduct>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories.clear();
                    for (CategoryProduct categoryProduct : response.body().getData()) {
                        if (categoryProduct.getStatus() != 1) {
                            categories.add(categoryProduct);
                        }
                    }
                    categoryProductAdapter = new CategoryProductAdapter(getContext(), categories);
                    recyclerView.setAdapter(categoryProductAdapter);
                    categoryProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CategoryProduct>>> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setControl(View view) {
        recyclerView = view.findViewById(R.id.rv);
        floatingActionButton = view.findViewById(R.id.fab);
    }
}