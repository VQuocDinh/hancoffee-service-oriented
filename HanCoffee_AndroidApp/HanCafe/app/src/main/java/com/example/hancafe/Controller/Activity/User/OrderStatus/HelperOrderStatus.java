package com.example.hancafe.Controller.Activity.User.OrderStatus;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Controller.Activity.Adapter.OrderStatusAdapter;
import com.example.hancafe.Model.OrderDetail;
import com.example.hancafe.Model.OrderManagement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HelperOrderStatus {
    public static void loadDataOrderStatus(Context context, RecyclerView rvProduct, LinearLayout lnEmpty, int idCategory){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String idUser = currentUser != null ? currentUser.getUid() : "";
        LinearLayoutManager linearLayoutManagerProduct = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvProduct.setLayoutManager(linearLayoutManagerProduct);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference orderManagementRef = database.getReference("Order_Management");
        Query query = orderManagementRef.orderByChild("idUser").equalTo(idUser);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<OrderManagement> orderManagements = new ArrayList<>();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        int status = dataSnapshot.child("idCategory").getValue(Integer.class);
                        if (status == idCategory) {
                            String date = dataSnapshot.child("date").getValue(String.class);
                            String idOrder = dataSnapshot.child("id").getValue(String.class);
                            int totalPrice = dataSnapshot.child("price").getValue(Integer.class);
                            String idUser = dataSnapshot.child("idUser").getValue(String.class);
                            OrderManagement orderManagement = new OrderManagement(status, totalPrice, date, idOrder, idUser);

                            if(!orderManagement.getId().isEmpty()){
                                lnEmpty.setVisibility(View.GONE);
                                rvProduct.setVisibility(View.VISIBLE);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference orderDetailRef = database.getReference("OrderDetail");
                                Query query = orderDetailRef.orderByChild("idOrder").equalTo(idOrder);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        List<OrderDetail> orderDetails = new ArrayList<>();
                                        for (DataSnapshot orderDetailSnaphot: snapshot.getChildren()){
                                            int size = orderDetailSnaphot.child("idSize").getValue(Integer.class);
                                            int priceProduct = orderDetailSnaphot.child("priceProduct").getValue(Integer.class);
                                            int quantity = orderDetailSnaphot.child("quantity").getValue(Integer.class);

                                            String idOrder = orderDetailSnaphot.child("idOrder").getValue(String.class);
                                            String imgProduct = orderDetailSnaphot.child("imgProduct").getValue(String.class);
                                            String nameProduct = orderDetailSnaphot.child("nameProduct").getValue(String.class);
                                            String idOrderDetail = orderDetailSnaphot.child("idOrderDetail").getValue(String.class);
                                            OrderDetail orderDetail = new OrderDetail(idOrderDetail,idOrder,imgProduct,nameProduct,size,quantity,priceProduct);
                                            orderDetails.add(orderDetail);
                                        }
                                        orderManagement.setOrderDetails(orderDetails);
                                        orderManagements.add(orderManagement);

                                        OrderStatusAdapter orderStatusAdapter = new OrderStatusAdapter(orderManagements, context);
                                        rvProduct.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                                        rvProduct.setAdapter(orderStatusAdapter);

                                        orderStatusAdapter.notifyDataSetChanged();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            } else {
                                lnEmpty.setVisibility(View.VISIBLE);
                                rvProduct.setVisibility(View.GONE);
                            }

                        }

                    }
                    OrderStatusAdapter orderStatusAdapter = new OrderStatusAdapter(orderManagements, context);
                    rvProduct.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                    rvProduct.setAdapter(orderStatusAdapter);

                    orderStatusAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
