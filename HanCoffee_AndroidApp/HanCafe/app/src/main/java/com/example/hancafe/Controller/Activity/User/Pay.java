package com.example.hancafe.Controller.Activity.User;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Controller.Activity.Adapter.OrderStatusAdapter;
import com.example.hancafe.Controller.Activity.Adapter.PayAdapter;
import com.example.hancafe.Model.CartItem;
import com.example.hancafe.Model.Promotion;
import com.example.hancafe.Model.OrderDetail;
import com.example.hancafe.Model.OrderManagement;
import com.example.hancafe.Controller.Payment.Zalo.Api.CreateOrder;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class Pay extends AppCompatActivity {

    private ImageView btnBack;
    private TextView btnUpdateAddress, tvTotalPrice, tvTen, tvSdt, tvDiaChi;
    private Button btnOrder, btnOrderPaymentZalo;
    private RecyclerView rvProduct;
    private Spinner spnPromotionCode;
    private PayAdapter payAdapter;
    List<CartItem> receivedList;
    private List<Promotion> promotions = new ArrayList<>();
    int totalPriceAfterDiscount = 0;
    int totalPriceAfterKM = 0;
    int totalPrice = 0;
    private Spinner spnDelivery, spnPay;
    List<String> shipMethod = new ArrayList<>();
    List<String> payMethod = new ArrayList<>();
    OrderStatusAdapter orderStatusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        paymentWithZalo();

        setControl();
        initData();
        setEvent();
    }

    private void paymentWithZalo() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2554, Environment.SANDBOX);
    }

    private void initData() {

        LinearLayoutManager linearLayoutManagerProduct = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvProduct.setLayoutManager(linearLayoutManagerProduct);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedList")) {

            String jsonString = intent.getStringExtra("selectedList");

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<CartItem>>() {
            }.getType();
            receivedList = gson.fromJson(jsonString, listType);

            payAdapter = new PayAdapter(receivedList, promotions);
            rvProduct.setAdapter(payAdapter);
            receivedList.size();
        }

        totalPrice = payAdapter.calculateTotalPrice();
        tvTotalPrice.setText(String.valueOf(totalPrice));

        // Khởi tạo danh sách chứa các tên khuyến mãi
        List<String> promotionNames = new ArrayList<>();
        promotionNames.add("Chưa chọn mã");

        // Kết nối với Firebase Realtime Database
        DatabaseReference promotionRef = FirebaseDatabase.getInstance().getReference().child("Promotion");

        // Lắng nghe sự kiện khi có thay đổi trong dữ liệu
        promotionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                // Lặp qua mỗi nút con trong dữ liệu Firebase
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Lấy đối tượng Promotion từ dataSnapshot
                    Promotion promotion = snapshot.getValue(Promotion.class);
                    // Kiểm tra nếu promotion không null và có tên
                    if (promotion != null && promotion.getName() != null) {
                        // Thêm tên khuyến mãi vào danh sách
                        promotionNames.add(promotion.getName());
                        // Thêm khuyến mãi vào danh sách promotions
                        promotions.add(promotion);
                    }
                }

                // Tạo ArrayAdapter để hiển thị danh sách các tên khuyến mãi trong Spinner
                ArrayAdapter<String> adapterPromotionNames = new ArrayAdapter<>(Pay.this, android.R.layout.simple_spinner_item, promotionNames);
                adapterPromotionNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnPromotionCode.setAdapter(adapterPromotionNames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });


    }

    private void setEvent() {

        shipMethod.add("Chuyển phát nhanh");
        shipMethod.add("Giao hàng tiết kiệm");
        payMethod.add("Chuyển khoản");
        payMethod.add("Tiền mặt");

        ArrayAdapter<String> adapterShipMethod = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shipMethod) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextSize(14);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextSize(14);
                return view;
            }
        };
//        ArrayAdapter<String> adapterPayMethod = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, payMethod) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                TextView textView = view.findViewById(android.R.id.text1);
//                textView.setTextSize(14); // Đặt kích thước chữ là 16dp
//                return view;
//            }
//
//            @Override
//            public View getDropDownView(int position, View convertView, ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView textView = view.findViewById(android.R.id.text1);
//                textView.setTextSize(14); // Đặt kích thước chữ là 16dp
//                return view;
//            }
//        };

        adapterShipMethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDelivery.setAdapter(adapterShipMethod);
        spnPromotionCode.setAdapter(adapterShipMethod);
//        spnPay.setAdapter(adapterPayMethod);

        btnOrder.setBackgroundColor(getResources().getColor(R.color.mainColor));
        btnOrderPaymentZalo.setBackgroundColor(getResources().getColor(R.color.zaloPay));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pay.this, MainActivity.class);
                intent.putExtra("openFragment", "cart");
                startActivity(intent);
            }
        });

        getInformationUser();

        btnUpdateAddress.setVisibility(View.GONE);
        btnUpdateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformationUser();
                if(tvTen.getText().toString().isEmpty() || tvSdt.getText().toString().isEmpty() || tvDiaChi.getText().toString().isEmpty()){
                    Toast.makeText(Pay.this, "Vui lòng cập nhật thông tin cá nhân để có thể đặt hàng", Toast.LENGTH_SHORT).show();
                } else {
                    orderSuccess(Pay.this);
                }

            }
        });

        btnOrderPaymentZalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvTen.getText().toString().isEmpty() || tvSdt.getText().toString().isEmpty() || tvDiaChi.getText().toString().isEmpty()){
                    Toast.makeText(Pay.this, "Vui lòng cập nhật thông tin cá nhân để có thể đặt hàng", Toast.LENGTH_SHORT).show();
                } else {
                    CreateOrder orderApi = new CreateOrder();

                    try {
                        JSONObject data = orderApi.createOrder(String.valueOf(totalPriceAfterDiscount));
                        String code = data.getString("return_code");

                        if (code.equals("1")) {

                            String token = data.getString("zp_trans_token");
                            ZaloPaySDK.getInstance().payOrder(Pay.this, token, "demozpdk://app", new PayOrderListener() {
                                @Override
                                public void onPaymentSucceeded(String s, String s1, String s2) {
                                    Toast.makeText(Pay.this, "Thanh toán thành công", Toast.LENGTH_SHORT);
                                    orderSuccess(Pay.this);
                                }

                                @Override
                                public void onPaymentCanceled(String s, String s1) {
                                    Toast.makeText(Pay.this, "Thanh toán thất bại", Toast.LENGTH_SHORT);
                                }

                                @Override
                                public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                    Toast.makeText(Pay.this, "Đã xảy ra sự cố trong quá trình thanh toán", Toast.LENGTH_SHORT);
                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        spnPromotionCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPromotion = parent.getItemAtPosition(position).toString();
                totalPriceAfterDiscount = payAdapter.calculateTotalPriceAfterDiscount(selectedPromotion);
                tvTotalPrice.setText(String.valueOf(totalPriceAfterDiscount));
                if (!selectedPromotion.equals("Chưa chọn mã")) {
                    for (Promotion promotion : promotions) {
                        if (promotion.getName().equals(selectedPromotion)) {
                            showPromotionDetailDialog(promotion);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có gì được chọn (nếu cần)
            }
        });


    }

    private void showPromotionDetailDialog(Promotion promotion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Pay.this);
        builder.setTitle(promotion.getName());
        builder.setMessage("Mức khuyến mãi: " + promotion.getDiscount() + "\n"
                + "Mô tả: " + promotion.getDescription() + "\n"
                + "Thời gian: " + promotion.getStartTime() + " - " + promotion.getEndTime());
        builder.setPositiveButton("Chọn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Hiển thị hộp thoại xác nhận nếu người dùng chọn "Chọn"
                showConfirmationDialog(promotion);
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đặt lại giá trị của Spinner về chưa chọn mã
                spnPromotionCode.setSelection(0);
                // Đóng dialog nếu người dùng chọn "Hủy"
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showConfirmationDialog(Promotion promotion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Pay.this);
        builder.setTitle("Xác nhận chọn mã khuyến mãi");
        builder.setMessage("Bạn có muốn chọn mã khuyến mãi \"" + promotion.getName() + "\"?");
        builder.setPositiveButton("Chọn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý khi người dùng chọn "Chọn"
                // Ví dụ: lưu mã khuyến mãi đã chọn vào biến hoặc thực hiện các hành động khác
                Toast.makeText(Pay.this, "Đã chọn mã khuyến mãi: " + promotion.getName(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng dialog nếu người dùng chọn "Hủy"
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void orderSuccess(Context context) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference orderManagementRef = firebaseDatabase.getReference("Order_Management");

        orderManagementRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String idUser = "";
                if (currentUser != null) {
                    idUser = currentUser.getUid();
                }
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String curentDay = day + "/" + month + "/" + year;

                DatabaseReference newRef = orderManagementRef.push();
                String id = newRef.getKey();
                OrderManagement orderManagement = new OrderManagement(1, totalPriceAfterDiscount, curentDay, id, idUser);
                orderManagementRef.child(id).setValue(orderManagement);
                for (CartItem cartItem : receivedList) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference orderDetailRef = firebaseDatabase.getReference("OrderDetail");
                    orderDetailRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int totalPriceProduct = cartItem.getProductPrice() * cartItem.getQuantity();
                            DatabaseReference newRef = orderDetailRef.push();
                            String idOrderDetail = newRef.getKey();
                            OrderDetail orderDetail = new OrderDetail(idOrderDetail, id, cartItem.getProductImg(), cartItem.getProductName(), cartItem.getSizeId(), cartItem.getQuantity(), totalPriceProduct);
                            orderDetailRef.child(idOrderDetail).setValue(orderDetail);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        for (CartItem cartItem : receivedList) {
            DatabaseReference cartDetailRef = FirebaseDatabase.getInstance().getReference("CartDetail");
            cartDetailRef.orderByChild("idCartItem").equalTo(cartItem.getIdCartItem()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        dataSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
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
//        Intent intent = new Intent(Pay.this, Orders.class);
//        startActivity(intent);
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage("Đặt hàng thành công")
                .setPositiveButton("Xem đơn hàng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Pay.this, Orders.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Tiếp tục mua hàng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Pay.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void showBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_address, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        Button btnUpdate = bottomSheetDialog.findViewById(R.id.btnUpdate);
        TextView tvTenUpdate = bottomSheetDialog.findViewById(R.id.tvTenUpdate);
        TextView tvSdtUpdate = bottomSheetDialog.findViewById(R.id.tvSdtUpdate);
        TextView tvDiaChiUpdate = bottomSheetDialog.findViewById(R.id.tvDiaChiUpdate);

//        btnUpdate.setBackgroundColor(getResources().getColor(R.color.mainColor));
//
//        tvTenUpdate.setText(tvTen.getText());
//        tvSdtUpdate.setText(tvSdt.getText());
//        tvDiaChiUpdate.setText(tvDiaChi.getText());
//
//
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (tvTenUpdate.getText() == null || tvSdtUpdate.getText() == null || tvDiaChiUpdate.getText() == null) {
//                    Toast.makeText(Pay.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
//
//                } else {
////                    tvTen.setText(tvTenUpdate.getText());
////                    tvSdt.setText(tvSdtUpdate.getText());
////                    tvDiaChi.setText(tvDiaChiUpdate.getText());
//
//
//                    bottomSheetDialog.cancel();
//                }
//            }
//        });
        bottomSheetDialog.show();
//        orderStatusAdapter.notifyDataSetChanged();

    }

    private void getInformationUser(){
        // Lấy thông tin về người dùng đang đăng nhập
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Lấy UID của người dùng đang đăng nhập
            String uid = currentUser.getUid();

            // Tìm kiếm thông tin người dùng trong cơ sở dữ liệu của bạn
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    // Kiểm tra xem dữ liệu có tồn tại không
                    if (snapshot.exists()) {
                        // Lấy tên của người dùng
                        String userName = snapshot.child("name").getValue(String.class);
                        String phone = snapshot.child("phone").getValue(String.class);
                        String address = snapshot.child("address").getValue(String.class);
                        // Hiển thị tên của người dùng
                        tvTen.setText(userName);
                        tvSdt.setText(phone);
                        tvDiaChi.setText(address);
                    } else {
                        // Hiển thị thông báo nếu không tìm thấy thông tin người dùng
                        Toast.makeText(Pay.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(Pay.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT);
                }
            });
        }
    }

    private void setControl() {
        btnUpdateAddress = findViewById(R.id.btnUpdateAddress);
        btnBack = findViewById(R.id.btnBack);
        btnOrder = findViewById(R.id.btnOrder);
        btnOrderPaymentZalo = findViewById(R.id.btnOrderPaymentZalo);
        rvProduct = findViewById(R.id.rvProduct);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        spnDelivery = findViewById(R.id.spnDelivery);
//        spnPay = findViewById(R.id.spnPay);
        tvTen = findViewById(R.id.tvTen);
        tvSdt = findViewById(R.id.tvSdt);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        spnPromotionCode = findViewById(R.id.spnPromotionCode);
    }

    private double calculateTotalPriceAfterDiscount(int totalPrice, String promotionCode) {
        // Kiểm tra nếu chưa chọn mã khuyến mãi
        if (promotionCode.equals("Chưa chọn mã")) {
            // Trả về tổng tiền ban đầu
            return totalPrice;
        }

        // Tìm kiếm mã khuyến mãi được chọn trong danh sách promotions
        for (Promotion promotion : promotions) {
            if (promotion.getCode().equals(promotionCode)) {
                // Áp dụng mức giảm giá tương ứng
                if (promotion.getDiscount().endsWith("%")) {
                    // Giảm giá phần trăm
                    double discountPercentage = Double.parseDouble(promotion.getDiscount().substring(0, promotion.getDiscount().length() - 1));
                    return totalPrice * (1 - discountPercentage / 100);
                } else if (promotion.getDiscount().endsWith("đ")) {
                    // Giảm giá cố định
                    int discountAmount = Integer.parseInt(promotion.getDiscount().substring(0, promotion.getDiscount().length() - 1));
                    return totalPriceAfterKM = totalPrice - discountAmount;
                }
            }
        }


        // Trả về tổng tiền ban đầu nếu không tìm thấy mã khuyến mãi
        return totalPrice;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}