package com.example.hancafe.Controller.Activity.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.hancafe.Controller.Activity.Auth.Home;
import com.example.hancafe.Model.User;
import com.example.hancafe.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainAdminActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    DrawerLayout drawerLayout;
    ImageButton imageButton;
    NavigationView navigationView;
    TextView textView, tvNameLogin;
    UserAdminFragment userAdminFragment = new UserAdminFragment();
    StaffAdminFragment staffAdminFragment = new StaffAdminFragment();
    ProductAdminFragment productAdminFragment = new ProductAdminFragment();
    CategoryProductAdminFragment categoryProductAdminFragment = new CategoryProductAdminFragment();
    OrderAdminFragment orderAdminFragment = new OrderAdminFragment();
    InformationPersonAdminFragment informationPersonAdminFragment = new InformationPersonAdminFragment();
    StatisticReportAdminFragment statisticReportAdminFragment = new StatisticReportAdminFragment();

    PromotionFragment promotionFragment = new PromotionFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        mAuth = FirebaseAuth.getInstance();
        setControl();
        setEvent();
    }

    private void setControl(){
        drawerLayout = findViewById(R.id.drawerLayout);
        imageButton = findViewById(R.id.imgBtnDrawerToggle);
        navigationView = findViewById(R.id.navigationView);
        textView = findViewById(R.id.txtToolBarChangeName);

        View headerView = navigationView.getHeaderView(0);
        tvNameLogin = headerView.findViewById(R.id.tvNameLogin);
    }

    private void setEvent(){
//        String uid = mAuth.getCurrentUser().getUid();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                User user = snapshot.getValue(User.class);
//                if(user.getName().isEmpty()){
//                    tvNameLogin.setText("Welcome, Admin");
//                    Toast.makeText(MainAdminActivity.this, "Welcome you to Dasboard Admin", Toast.LENGTH_SHORT).show();
//                } else {
//                    tvNameLogin.setText("Welcome, " + user.getName());
//                    Toast.makeText(MainAdminActivity.this, "Welcome " + user.getName() + " to Dasboard Admin", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Xử lý lỗi khi không thể lấy dữ liệu từ Firebase
//                Toast.makeText(MainAdminActivity.this, "Không thể lấy dữ liệu người dùng", Toast.LENGTH_SHORT).show();
//            }
//        });

        getSupportFragmentManager().beginTransaction().replace(R.id.container_admin, informationPersonAdminFragment).commit();
        textView.setText("Thông tin cá nhân");

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.nav_information_person){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_admin, informationPersonAdminFragment).commit();
                    textView.setText("Thông tin cá nhân");
                } else if (itemId == R.id.nav_user) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_admin, userAdminFragment).commit();
                    textView.setText("Khách hàng");
                } else if (itemId == R.id.nav_staff) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_admin, staffAdminFragment).commit();
                    textView.setText("Nhân viên");
                } else if (itemId == R.id.nav_product) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_admin, productAdminFragment).commit();
                    textView.setText("Sản phẩm");
                } else if (itemId == R.id.nav_category_product) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_admin, categoryProductAdminFragment).commit();
                    textView.setText("Danh mục sản phẩm");
                } else if (itemId == R.id.nav_order) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_admin, orderAdminFragment).commit();
                    textView.setText("Đặt hàng");
                } else if (itemId == R.id.nav_statistic_report) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_admin,  statisticReportAdminFragment).commit();
                    textView.setText("Báo cáo thống kê");
                } else if (itemId == R.id.nav_promotion_management) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_admin,  promotionFragment).commit();
                    textView.setText("Quản lý khuyến mãi");
                } else if (itemId == R.id.nav_logout) {
//                    mAuth.signOut();
                    Intent intent = new Intent(MainAdminActivity.this, Home.class);
                    startActivity(intent);
                    Toast.makeText(MainAdminActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.close();
                return false;
            }
        });
    }
}