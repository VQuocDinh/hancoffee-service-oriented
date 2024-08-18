package com.example.hancafe.Controller.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import com.example.hancafe.Controller.Activity.Adapter.OrderManagementUserViewPagerAdapter;
import com.example.hancafe.R;


public class Orders extends AppCompatActivity {

    private ImageView btnBack;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    OrderManagementUserViewPagerAdapter orderManagementViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        setControl();
        setEvent();
    }

    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Orders.this, MainActivity.class);
                startActivity(intent);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }




    private void setControl() {

        btnBack = findViewById(R.id.btnBack);

        //Chuyển đổi giữa các tabItem
        tabLayout = findViewById(R.id.tlOrder);
        viewPager2 = findViewById(R.id.vpOrder);
        orderManagementViewPagerAdapter = new OrderManagementUserViewPagerAdapter(Orders.this);
        viewPager2.setAdapter(orderManagementViewPagerAdapter);
    }


}