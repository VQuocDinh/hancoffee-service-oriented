package com.example.hancafe.Controller.Activity.Admin.Order.Management;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Controller.Activity.Adapter.OrderManagementAdminAdapter;
import com.example.hancafe.Model.OrderDetail;
import com.example.hancafe.Model.OrderManagement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HelperOrderManagement {
    public static void loadDataOrderManagement(Context context, RecyclerView rcvCategoryOrderManagement, LinearLayout lnEmpty, int statusCategory){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference orderManagementRef = database.getReference("Order_Management");

        orderManagementRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<OrderManagement> orderManagementList = new ArrayList<>();
                orderManagementList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int status = dataSnapshot.child("idCategory").getValue(Integer.class);
                    if (status == statusCategory) {
                        String date = dataSnapshot.child("date").getValue(String.class);
                        String idOrder = dataSnapshot.child("id").getValue(String.class);
                        int totalPrice = dataSnapshot.child("price").getValue(Integer.class);
                        String idUser = dataSnapshot.child("idUser").getValue(String.class);
                        OrderManagement orderManagement = new OrderManagement(status, totalPrice, date, idOrder, idUser);

                        if(!orderManagement.getId().isEmpty()){
                            lnEmpty.setVisibility(View.GONE);
                            rcvCategoryOrderManagement.setVisibility(View.VISIBLE);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference orderDetailRef = database.getReference("OrderDetail");
                            Query query = orderDetailRef.orderByChild("idOrder").equalTo(idOrder);
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    List<OrderDetail> orderDetailList = new ArrayList<>();
                                    orderDetailList.clear();

                                    for (DataSnapshot orderDetailSnaphot: snapshot.getChildren()){
                                        int size = orderDetailSnaphot.child("idSize").getValue(Integer.class);
                                        int priceProduct = orderDetailSnaphot.child("priceProduct").getValue(Integer.class);
                                        int quantity = orderDetailSnaphot.child("quantity").getValue(Integer.class);

                                        String idOrder = orderDetailSnaphot.child("idOrder").getValue(String.class);
                                        String imgProduct = orderDetailSnaphot.child("imgProduct").getValue(String.class);
                                        String nameProduct = orderDetailSnaphot.child("nameProduct").getValue(String.class);
                                        String idOrderDetail = orderDetailSnaphot.child("idOrderDetail").getValue(String.class);
                                        OrderDetail orderDetail = new OrderDetail(idOrderDetail,idOrder,imgProduct,nameProduct,size,quantity,priceProduct);
                                        orderDetailList.add(orderDetail);
                                    }
                                    orderManagement.setOrderDetails(orderDetailList);
                                    orderManagementList.add(orderManagement);

                                    OrderManagementAdminAdapter orderManagementAdapter = new OrderManagementAdminAdapter(context, orderManagementList);

                                    rcvCategoryOrderManagement.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                                    rcvCategoryOrderManagement.setAdapter(orderManagementAdapter);
                                    orderManagementAdapter.notifyDataSetChanged();

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        } else {
                            lnEmpty.setVisibility(View.VISIBLE);
                            rcvCategoryOrderManagement.setVisibility(View.GONE);
                        }

                    }
                }
                OrderManagementAdminAdapter orderManagementAdapter = new OrderManagementAdminAdapter(context, orderManagementList);

                rcvCategoryOrderManagement.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                rcvCategoryOrderManagement.setAdapter(orderManagementAdapter);
                orderManagementAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}

//
//        orderManagementAdapter= new OrderManagementAdminAdapter(context, list);
//        rcvCategoryOrderManagement.setLayoutManager(new LinearLayoutManager(context));
//        rcvCategoryOrderManagement.setAdapter(orderManagementAdapter);
//
//        // Lấy idCategory cho fragment hiện tại
//        int categoryId = 1;
//
//        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Order_Management");
//
//        // Tạo query để lọc theo idCategory
//        Query query = orderRef.orderByChild("idCategory").equalTo(categoryId);
//
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//
//                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
//                    String id = orderSnapshot.child("id").getValue(String.class);
//                    String name = orderSnapshot.child("name").getValue(String.class);
////                    int price = orderSnapshot.child("price").getValue(Integer.class);
//                    Integer priceInteger = orderSnapshot.child("price").getValue(Integer.class);
//                    int price;
//                    if (priceInteger != null) {
//                        price = priceInteger.intValue();
//                        Log.d("Error", "Không lỗi");
//                    } else {
//                        price = 0;
//                        Log.d("Error", "Lỗi hehe");
//                    }
//                    String dateTime = orderSnapshot.child("date").getValue(String.class);
//                    String picture = orderSnapshot.child("picure").getValue(String.class);
//                    int idCategory = orderSnapshot.child("idCategory").getValue(Integer.class);
//                    String idUser = orderSnapshot.child("idUser").getValue(String.class);
//
//                    OrderManagement order = new OrderManagement(id, name, price, dateTime, picture, idCategory, idUser);
//                    list.add(order);
////                    OrderManagement order = orderSnapshot.getValue(OrderManagement.class);
////                    if (order != null) {
////                        list.add(order);
////                    }
//                }
//                orderManagementAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        orderManagementAdapter.setOnItemClickListener(new OrderManagementAdminAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(OrderManagement order) {
//                // Mở BottomSheetDialog và truyền dữ liệu của order được click vào đó
//                BottomSheetDialogOrderManagement bottomSheetDialog = BottomSheetDialogOrderManagement.newInstance(order);
//                bottomSheetDialog.show(getChildFragmentManager(), bottomSheetDialog.getTag());
//            }
//        });
