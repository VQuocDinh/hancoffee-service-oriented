package com.example.hancafe.Controller.Activity.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.hancafe.Controller.Activity.Adapter.CartItemAdapter;
import com.example.hancafe.Controller.Activity.Adapter.ProductsAdapter;
import com.example.hancafe.Controller.Api.ApiResponse;
import com.example.hancafe.Controller.Api.ApiService;
import com.example.hancafe.Model.CartData;
import com.example.hancafe.Model.CartItem;
import com.example.hancafe.Model.User;
import com.example.hancafe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment implements ProductsAdapter.OnItemClickListener {
    private ImageView btnBack;
    private Button btnOrderProceed;
    private RecyclerView rvProduct;
    private CheckBox cbSelectAll;
    private TextView tvEmpty;
    LinearLayout lnCart, lnCartEmpty;
    List<CartItem> cartItems = new ArrayList<>();
    CartItemAdapter cartItemAdapter;
    int countItem = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Đọc ID người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");


        btnOrderProceed = view.findViewById(R.id.btnOrderProceed);
        btnBack = view.findViewById(R.id.btnBack);
        rvProduct = view.findViewById(R.id.rvProduct);
        cbSelectAll = view.findViewById(R.id.cbSelectAll);

        lnCart = view.findViewById(R.id.lnCart);
        lnCartEmpty = view.findViewById(R.id.lnEmpty);

        lnCartEmpty.setVisibility(View.GONE);

        tvEmpty = view.findViewById(R.id.tvEmpty);

//        ApiService apiService = ApiService.apiService;
//        Call<ApiResponse<List<User>>> call = apiService.getUsers();
//        call.enqueue(new Callback<ApiResponse<List<User>>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<List<User>>> call, Response<ApiResponse<List<User>>> response) {
//                if(response.isSuccessful()){
//                    List<User> users = new ArrayList<>();
//                    for (User user: response.body().getData()){
////                        if(user.getId())
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<List<User>>> call, Throwable throwable) {
//
//            }
//        });
        initCartDetail(userId);
        setEvent();
        return view;
    }

    private void setEvent() {
        btnOrderProceed.setBackgroundColor(getResources().getColor(R.color.mainColor));
        btnOrderProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                for (int i = 0; i < rvProduct.getChildCount(); i++) {
                    View view = rvProduct.getChildAt(i);
                    CheckBox checkBox = view.findViewById(R.id.cbProduct);
                    if (checkBox.isChecked()) {
                        count++;
                    }
                }
                if (count <= 0) {
                    Toast.makeText(getContext(), "Chưa có sản phẩm nào được chọn", Toast.LENGTH_SHORT).show();
                } else {
                    List<CartItem> selectedList = new ArrayList<>(cartItemAdapter.getSelectedList());
                    Gson gson = new Gson();
                    String jsonSelectedList = gson.toJson(selectedList);
                    Intent intent = new Intent(getActivity(), Pay.class);
                    intent.putExtra("selectedList", jsonSelectedList);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < rvProduct.getChildCount(); i++) {
                    View view = rvProduct.getChildAt(i);
                    CheckBox checkBox = view.findViewById(R.id.cbProduct);
                    checkBox.setChecked(isChecked);
                }
            }
        });

    }

    private void initCartDetail(String userId) {
        ApiService apiService = ApiService.apiService;
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("userId", userId);

        Call<ApiResponse<CartData>> call = apiService.getCart(requestBody);
        call.enqueue(new Callback<ApiResponse<CartData>>() {
            @Override
            public void onResponse(Call<ApiResponse<CartData>> call, Response<ApiResponse<CartData>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<CartData> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getData() != null) {
                        CartData cartData = apiResponse.getData();

                        if (cartData.isSuccess()) {
                            Map<String, Integer> cartItemsMap = cartData.getCartData();
                            List<CartItem> cartItemList = new ArrayList<>();

                            for (Map.Entry<String, Integer> entry : cartItemsMap.entrySet()) {
                                String productId = entry.getKey();
                                int quantity = entry.getValue();

                                // Tạo đối tượng CartItem và thêm vào danh sách
//                                CartItem cartItem = new CartItem(productId, quantity);
//                                cartItemList.add(cartItem);
                            }

                            // Cập nhật dữ liệu cho RecyclerView
//                            cartItemAdapter.setCartItems(cartItemList);
                            cartItemAdapter.notifyDataSetChanged();

//                            if (cartItemList.isEmpty()) {
//                                lnCart.setVisibility(View.GONE);
//                                lnCartEmpty.setVisibility(View.VISIBLE);
//                            } else {
//                                lnCart.setVisibility(View.VISIBLE);
//                                lnCartEmpty.setVisibility(View.GONE);
//                            }
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Đã xảy ra lỗi khi lấy dữ liệu giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CartData>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//        Call<ApiResponse<User>> call = apiService.getCart(userId);
//        System.out.println(userId);
//        call.enqueue(new Callback<ApiResponse<User>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
//                if (response.isSuccessful()) {
//                    ApiResponse<User> apiResponse = response.body();
//
//                } else {
//                    // Xử lý khi có lỗi trả về từ API
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
//                // Xử lý khi gọi API thất bại
//            }
//        });
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//        String idUser = "";
//        if (currentUser != null) {
//            idUser = currentUser.getUid();
//        }
        LinearLayoutManager linearLayoutManagerProduct = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvProduct.setLayoutManager(linearLayoutManagerProduct);

        cartItems = new ArrayList<>();

        cartItemAdapter = new CartItemAdapter(cartItems);
        rvProduct.setAdapter(cartItemAdapter);

//        DatabaseReference cartDetailRef = FirebaseDatabase.getInstance().getReference("CartDetail");
//
//        cartDetailRef.orderByChild("idCart").equalTo(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Giỏ hàng không trống
//                    lnCart.setVisibility(View.VISIBLE);
//                    lnCartEmpty.setVisibility(View.GONE);
//
//                    for (DataSnapshot cartItemSnapshot : dataSnapshot.getChildren()) {
//                        String idProduct = cartItemSnapshot.child("idProduct").getValue(String.class);
//                        int idSize = cartItemSnapshot.child("idSize").getValue(Integer.class);
//                        int quantity = cartItemSnapshot.child("quantity").getValue(Integer.class);
//                        int idCartItem = cartItemSnapshot.child("idCartItem").getValue(Integer.class);
//
//                        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products").child(idProduct);
//                        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot productSnapshot) {
//                                if (productSnapshot.exists()) {
//                                    String productName = productSnapshot.child("name").getValue(String.class);
//                                    String productImg = productSnapshot.child("purl").getValue(String.class);
//                                    int productPrice = productSnapshot.child("price").getValue(Integer.class);
//
//                                    CartItem cartItem = new CartItem(idProduct, quantity, idSize, idCartItem, productPrice, productName, productImg);
//                                    cartItems.add(cartItem);
//                                    cartItemAdapter.notifyItemInserted(cartItems.size() - 1);
//                                    countItem++;
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                Log.e("FirebaseData", "Lỗi khi truy cập dữ liệu từ Firebase: " + error.getMessage());
//                            }
//                        });
//                    }
//                } else {
//                    // Giỏ hàng trống
//                    lnCart.setVisibility(View.GONE);
//                    lnCartEmpty.setVisibility(View.VISIBLE);
//
//                    String setTitle = getResources().getString(R.string.title_order_status_product_empty);
//                    tvEmpty.setText(setTitle);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("FirebaseData", "Lỗi khi truy cập dữ liệu từ Firebase: " + error.getMessage());
//            }
//        });
        cartItemAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemProductClick(int position) {
        List<CartItem> cartItemList = cartItemAdapter.getData();
        CartItem cartItem = cartItemList.get(position);
        Intent intent = new Intent(getActivity(), ProductDetail.class);
        intent.putExtra("cartItem", cartItem);
        startActivity(intent);
    }
}