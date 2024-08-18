package com.example.hancafe.Controller.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hancafe.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity{
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    CartFragment cartFragment = new CartFragment();
//    OrderFragment orderFragment = new OrderFragment();
    OtherFragment otherFragment = new OtherFragment();
    CategoryFragment categoryFragment = new CategoryFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();
    }

    private void setEvent() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                    return true;
                } else if (itemId == R.id.cart) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, cartFragment).commit();
                    return true;
                } else if (itemId == R.id.category) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, categoryFragment).commit();
                    return true;
                } else if (itemId == R.id.other) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, otherFragment).commit();
                    return true;
                }

                return false;
            }
        });

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("openFragment")) {
            String fragmentToOpen = intent.getStringExtra("openFragment");

            if(fragmentToOpen.equals("cart")) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, cartFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }
    private void setControl() {
        bottomNavigationView = findViewById(R.id.bottomNavView);
    }
}