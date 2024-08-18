package com.example.hancafe.Controller.Activity.Admin;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Controller.Activity.Adapter.OrderManagementAdminAdapter;
import com.example.hancafe.Controller.Activity.Adapter.OrderStatusAdapter;
import com.example.hancafe.Controller.Log.FileLogger;
import com.example.hancafe.Model.OrderDetail;
import com.example.hancafe.Model.OrderManagement;
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

import java.util.ArrayList;

public class BillAdmin extends AppCompatActivity {
    private ImageView btnBack;
    private TextView tvIdOrder, tvStatus, tvDateOrder, tvTotalPrice, tvNameReceiver, tvSdtReceiver, tvAddressReceiver;
    private Button btnConfirmOrderManagement, btnDeliveryOrderManagement, btnCompleteOrderManagement, btnCancelOrderManagement;
    private RecyclerView rvProduct;
    OrderManagementAdminAdapter orderManagementAdminAdapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_admin);
        setControl();
        setEvent();
        initData();
        log();
    }
    private void log() {
        // Lấy instance của FileLogger
        FileLogger fileLogger = FileLogger.getFileLogger(getApplicationContext());
        // Ghi log
        fileLogger.log("Bill Admin created.");
    }

    private void initData() {
        Intent intent = getIntent();
        OrderManagement orderManagement = intent.getParcelableExtra("orderManagement");
        ArrayList<OrderDetail> orderDetails = intent.getParcelableArrayListExtra("orderDetails");
        LinearLayoutManager linearLayoutManagerProduct = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvProduct.setLayoutManager(linearLayoutManagerProduct);
        OrderStatusAdapter.OrderDetailAdapter orderDetailAdapter = new OrderStatusAdapter.OrderDetailAdapter(orderDetails);
        orderDetailAdapter.notifyDataSetChanged();
        rvProduct.setAdapter(orderDetailAdapter);
        tvIdOrder.setText(orderManagement.getId());
        if (orderManagement.getIdCategory() == 1) {
            tvStatus.setText("Chờ xác nhận");
        } else if (orderManagement.getIdCategory() == 2) {
            tvStatus.setText("Đang vận chuyển");
        } else if (orderManagement.getIdCategory() == 3) {
            tvStatus.setText("Đã giao");
        } else {
            tvStatus.setText("Đã hủy");
        }
        tvDateOrder.setText(orderManagement.getDate());
        tvTotalPrice.setText(String.valueOf(orderManagement.getPrice()));

        // Lấy thông tin về người dùng đang đăng nhập
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Lấy UID của người dùng đang đăng nhập
            String uid = orderManagement.getIdUser();

            // Tìm kiếm thông tin người dùng trong cơ sở dữ liệu của bạn
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Kiểm tra xem dữ liệu có tồn tại không
                    if (snapshot.exists()) {
                        // Lấy tên của người dùng
                        String userName = snapshot.child("name").getValue(String.class);
                        String phone = snapshot.child("phone").getValue(String.class);
                        String address = snapshot.child("address").getValue(String.class);
                        // Hiển thị tên của người dùng
                        tvNameReceiver.setText(userName);
                        tvSdtReceiver.setText(phone);
                        tvAddressReceiver.setText(address);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(BillAdmin.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT);
                }
            });
        }
    }

    private void setEvent() {

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirmOrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOrderAction(1);
            }
        });
        btnDeliveryOrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOrderAction(2);
            }
        });
        btnCompleteOrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOrderAction(3);
            }
        });
        btnCancelOrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOrderAction(4);
            }
        });
    }

    private void handleOrderAction(int newIdCategory) {
        OrderManagement orderManagement = getIntent().getParcelableExtra("orderManagement");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Xác nhận chuyển trạng thái đơn hàng!")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        String idOrder = orderManagement.getId();
//                        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Order_Management").child(idOrder);
//
//                        Map<String, Object> updateData = new HashMap<>();
//                        updateData.put("idCategory", newIdCategory);
//                        orderRef.updateChildren(updateData);
//
//                        finish();
                        String idOrder = orderManagement.getId();
                        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Order_Management");
                        orderRef.orderByChild("id").equalTo(idOrder).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    dataSnapshot.getRef().child("idCategory").setValue(newIdCategory).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(BillAdmin.this, "Chuyển trạng thái đơn hàng thành công.", Toast.LENGTH_SHORT);
                                            finish();
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
                .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setControl() {
        btnBack = findViewById(R.id.btnBack);
        rvProduct = findViewById(R.id.rvProduct);
        tvIdOrder = findViewById(R.id.tvIdOrder);
        tvStatus = findViewById(R.id.tvStatus);
        tvDateOrder = findViewById(R.id.tvDateOrder);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvNameReceiver = findViewById(R.id.tvNameReceiver);
        tvSdtReceiver = findViewById(R.id.tvSdtReceiver);
        tvAddressReceiver = findViewById(R.id.tvAddressReceiver);

        btnConfirmOrderManagement = findViewById(R.id.btnConfirmOrderManagement);
        btnDeliveryOrderManagement = findViewById(R.id.btnDeliveryOrderManagement);
        btnCompleteOrderManagement = findViewById(R.id.btnCompleteOrderManagement);
        btnCancelOrderManagement = findViewById(R.id.btnCancelOrderManagement);
    }
}