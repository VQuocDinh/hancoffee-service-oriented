package com.example.hancafe.Controller.Activity.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hancafe.Controller.Activity.Adapter.OrderManagementAdminAdapter;
import com.example.hancafe.Model.OrderManagement;
import com.example.hancafe.Model.User;
import com.example.hancafe.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BottomSheetDialogOrderManagement extends BottomSheetDialogFragment {
    private OrderManagementAdminAdapter orderManagementAdapter;
    private ArrayList<OrderManagement> list;
    private static final String KEY_ORDER_MANAGEMENT = "order_management_object";
    private OrderManagement orderManagement;
    private User user;
    private TextView tvBtsCategoryItemName, tvBtsCategoryItemPrice, tvBtsCategoryItemDateTime, tvBtsCategoryItemId;
    private ImageView ivBtsCategoryItemImage;

    private Spinner spnConvertStatusOrderManagement;
    private Button btnHuyConvertOrderManagement, btnSubmitConvertOrderManagement;

    private Button btnConfirmOrderManagement, btnDeliveryOrderManagement, btnCompleteOrderManagement, btnCancelOrderManagement;

    DatabaseReference databaseReference;

    private int currentCategoryId; // Biến lưu trữ idCategory hiện tại

    public static BottomSheetDialogOrderManagement newInstance(OrderManagement orderManagement){
        BottomSheetDialogOrderManagement bottomSheetDialogOrderManagement = new BottomSheetDialogOrderManagement();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ORDER_MANAGEMENT, orderManagement);

        bottomSheetDialogOrderManagement.setArguments(bundle);
        return bottomSheetDialogOrderManagement;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleReceive = getArguments();

        if (bundleReceive != null){
            orderManagement = (OrderManagement) bundleReceive.get(KEY_ORDER_MANAGEMENT);
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet_dialog_order_management ,null);

        bottomSheetDialog.setContentView(viewDialog);

        initView(viewDialog);
        setDataOrderManagement();

        return bottomSheetDialog;
    }
    private void initView(View view) {
        tvBtsCategoryItemId = view.findViewById(R.id.tvBtsCategoryItemId);
        tvBtsCategoryItemName = view.findViewById(R.id.tvBtsCategoryItemName);
        tvBtsCategoryItemPrice = view.findViewById(R.id.tvBtsCategoryItemPrice);
        tvBtsCategoryItemDateTime = view.findViewById(R.id.tvBtsCategoryItemDateTime);
        ivBtsCategoryItemImage = view.findViewById(R.id.ivBtsCategoryItemImage);

        // Ánh xạ view từ layout
        btnConfirmOrderManagement = view.findViewById(R.id.btnConfirmOrderManagement);
        btnDeliveryOrderManagement = view.findViewById(R.id.btnDeliveryOrderManagement);
        btnCompleteOrderManagement = view.findViewById(R.id.btnCompleteOrderManagement);
        btnCancelOrderManagement = view.findViewById(R.id.btnCancelOrderManagement);

    }

    private void setDataOrderManagement() {
        if(orderManagement == null){
            return;
        }
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        databaseReference.child("Order_Management").limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        String userId = snapshot.child("idUser").getValue(String.class);
//                        if (userId != null) {
//                            // Tìm thấy idUser, bạn có thể tiếp tục xử lý ở đây
//                            Log.d("UserId", userId);
//
//                            // Tiếp tục truy vấn thông tin người dùng hoặc thực hiện các hành động khác ở đây
//                            databaseReference.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.exists()) {
//                                        String email = dataSnapshot.child("email").getValue(String.class);
//                                        if (email != null) {
//                                            // Đã có được email, bạn có thể làm gì đó với nó ở đây
////                                            Log.d("Email", email);
//                                            tvBtsCategoryItemId.setText(email);
//                                        } else {
//                                            Log.d("Email", "Không tìm thấy email");
//                                        }
//                                    } else {
//                                        Log.d("User", "Không tìm thấy thông tin người dùng");
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                    Log.d("Error", databaseError.getMessage());
//                                }
//                            });
//                        } else {
//                            Log.d("UserId", "Không tìm thấy idUser trong Order_Management");
//                        }
//                        // Chỉ lấy một mục đầu tiên và kết thúc vòng lặp
//                        break;
//                    }
//                } else {
//                    Log.d("Order Management", "Không có dữ liệu trong Order_Management");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d("Error", databaseError.getMessage());
//            }
//        });


//        tvBtsCategoryItemId.setText(user.getEmail());
//        tvBtsCategoryItemName.setText(orderManagement.getName());
        // Cập nhật định dạng tiền tệ
        int price = orderManagement.getPrice();
        DecimalFormat df = new DecimalFormat("###,###.##");
        String formattedPrice = df.format(price) + "đ";
        tvBtsCategoryItemPrice.setText(formattedPrice);

//        String dateTimeStr = orderManagement.getDateTime();
//        String formattedDateTime = dateTimeStr.replace("-", "/");
//        tvBtsCategoryItemDateTime.setText(formattedDateTime);
        tvBtsCategoryItemDateTime.setText(orderManagement.getDate());

        //tải ảnh từ firebase
//        String urlImage = orderManagement.getPicure();
//        Glide.with(getContext()).load(urlImage).transform(new CircleCrop()).placeholder(R.drawable.milk_coffee).into(ivBtsCategoryItemImage);

        // Lấy DatabaseReference cho Firebase
//        databaseReference = FirebaseDatabase.getInstance().getReference("Order_Management");
        list = new ArrayList<>();
        orderManagementAdapter = new OrderManagementAdminAdapter(getContext(), list);

        // Đặt lắng nghe sự kiện click cho từng nút
        btnConfirmOrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cập nhật idCategory trong Firebase thành 1

                int newIdCategory = 1; // idCategory cho trạng thái "Confirm"

                String currentOrderId = orderManagement.getId();

                databaseReference = FirebaseDatabase.getInstance().getReference("Order_Management").child(currentOrderId);

                Map<String, Object> updateData = new HashMap<>();
                updateData.put("idCategory", newIdCategory);
                databaseReference.updateChildren(updateData);

                // Cập nhật adapter để hiển thị trạng thái mới
                orderManagementAdapter.notifyItemChanged(newIdCategory);

                dismiss();
            }
        });

        btnDeliveryOrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cập nhật idCategory trong Firebase thành 2

                int newIdCategory = 2; // idCategory cho trạng thái "Delivery"

                String currentOrderId = orderManagement.getId();

                databaseReference = FirebaseDatabase.getInstance().getReference("Order_Management").child(currentOrderId);

                Map<String, Object> updateData = new HashMap<>();
                updateData.put("idCategory", newIdCategory);
                databaseReference.updateChildren(updateData);

                // Cập nhật adapter để hiển thị trạng thái mới
                orderManagementAdapter.notifyItemChanged(newIdCategory);

                dismiss();
            }
        });

        btnCompleteOrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cập nhật idCategory trong Firebase thành 3
                int newIdCategory = 3; // idCategory cho trạng thái "Complete"

                String currentOrderId = orderManagement.getId();

                databaseReference = FirebaseDatabase.getInstance().getReference("Order_Management").child(currentOrderId);

                Map<String, Object> updateData = new HashMap<>();
                updateData.put("idCategory", newIdCategory);
                databaseReference.updateChildren(updateData);

                // Cập nhật adapter để hiển thị trạng thái mới
                orderManagementAdapter.notifyItemChanged(newIdCategory);

                dismiss();
            }
        });

        btnCancelOrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cập nhật idCategory trong Firebase thành 4
                int newIdCategory = 4; // idCategory cho trạng thái "Cancel"

                String currentOrderId = orderManagement.getId();

                databaseReference = FirebaseDatabase.getInstance().getReference("Order_Management").child(currentOrderId);

                Map<String, Object> updateData = new HashMap<>();
                updateData.put("idCategory", newIdCategory);
                databaseReference.updateChildren(updateData);

                // Cập nhật adapter để hiển thị trạng thái mới
                orderManagementAdapter.notifyItemChanged(newIdCategory);

                dismiss();
            }
        });

    }

}
