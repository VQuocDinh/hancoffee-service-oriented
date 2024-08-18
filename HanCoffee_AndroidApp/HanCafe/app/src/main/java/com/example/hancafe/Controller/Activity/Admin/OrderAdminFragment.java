package com.example.hancafe.Controller.Activity.Admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hancafe.Controller.Activity.Adapter.OrderManagementAdminViewPagerAdapter;
import com.example.hancafe.R;
import com.google.android.material.tabs.TabLayout;

public class OrderAdminFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    OrderManagementAdminViewPagerAdapter orderViewPagerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_order_management, container, false);

        //Chuyển đổi giữa các tabItem
        tabLayout = view.findViewById(R.id.tlOrder);
        viewPager2 = view.findViewById(R.id.vpOrder);
        orderViewPagerAdapter = new OrderManagementAdminViewPagerAdapter(OrderAdminFragment.this);
        viewPager2.setAdapter(orderViewPagerAdapter);

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

        return view;
    }
}