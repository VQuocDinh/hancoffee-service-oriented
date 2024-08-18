package com.example.hancafe.Controller.Activity.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hancafe.Controller.Activity.Auth.Home;

import com.example.hancafe.Controller.Activity.User.PersonInformation.MainPersonInformationActivity;
import com.example.hancafe.R;
import com.google.firebase.auth.FirebaseAuth;

public class OtherFragment extends Fragment {
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_other, container, false);

        // Lịch sử đơn hàng
        LinearLayout lnTienIch1 = view.findViewById(R.id.lnTienIch1);
        lnTienIch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Orders.class);
                startActivity(intent);
            }
        });

        //Điều khoản
        LinearLayout lnTienIch2 = view.findViewById(R.id.lnTienIch2);
        lnTienIch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://thecoffeehouse.com/pages/dieu-khoan-su-dung";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        //Thông tin cá nhân
        Button btnThongTinCaNhan = view.findViewById(R.id.btnThongTinCaNhan);
        btnThongTinCaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainPersonInformationActivity.class);
                startActivity(intent);
            }
        });

        //Đăng xuất
        Button btnDangXuat = view.findViewById(R.id.btnDangXuat);

        mAuth = FirebaseAuth.getInstance(); // Khởi tạo FirebaseAuth
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa hết activity trước đó
                startActivity(intent);
                Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}