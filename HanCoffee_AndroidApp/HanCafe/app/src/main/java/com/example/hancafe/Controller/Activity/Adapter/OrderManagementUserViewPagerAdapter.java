package com.example.hancafe.Controller.Activity.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hancafe.Controller.Activity.User.OrderStatus.CancelOrderFragment;
import com.example.hancafe.Controller.Activity.User.OrderStatus.CompleteOrderFragment;
import com.example.hancafe.Controller.Activity.User.OrderStatus.ConfirmOrderFragment;
import com.example.hancafe.Controller.Activity.User.OrderStatus.DeliveryOrderFragment;


public class OrderManagementUserViewPagerAdapter extends FragmentStateAdapter {

    public OrderManagementUserViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ConfirmOrderFragment();
            case 1:
                return new DeliveryOrderFragment();
            case 2:
                return new CompleteOrderFragment();
            case 3:
                return new CancelOrderFragment();
            default:
                return new ConfirmOrderFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
