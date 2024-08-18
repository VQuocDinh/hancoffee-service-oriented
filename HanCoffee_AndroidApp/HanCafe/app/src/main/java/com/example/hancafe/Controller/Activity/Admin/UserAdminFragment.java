package com.example.hancafe.Controller.Activity.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hancafe.Controller.Activity.Adapter.StaffAdapter;
import com.example.hancafe.Controller.Activity.Adapter.UserAdapter;
import com.example.hancafe.Controller.Api.ApiResponse;
import com.example.hancafe.Controller.Api.ApiService;
import com.example.hancafe.Controller.Log.FileLogger;
import com.example.hancafe.Model.User;
import com.example.hancafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdminFragment extends Fragment {
    RecyclerView rcvUser;
    TextView tvUserName;
    Spinner spnUser;
    List<User> users;
    UserAdapter userAdapter;
    ImageButton imgBtnSaveRole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_user, container, false);

        setControl(view);
        setEvent();
        log();
        return view;
    }

    private void log() {
        // Lấy instance của FileLogger
        FileLogger fileLogger = FileLogger.getFileLogger(getContext());
        // Ghi log
        fileLogger.log("Customer Admin Fragment created.");
    }

    private void setEvent() {

        rcvUser.setLayoutManager(new LinearLayoutManager(getContext()));

        ApiService apiService = ApiService.apiService;
        Call<ApiResponse<List<User>>> call = apiService.getCustomers();

        call.enqueue(new Callback<ApiResponse<List<User>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<User>>> call, Response<ApiResponse<List<User>>> response) {
                if (response.isSuccessful()){
                    users = new ArrayList<>();
                    for (User user: response.body().getData()){
                        users.add(user);
                    }
                    userAdapter = new UserAdapter(getContext(), users, true);
                    rcvUser.setAdapter(userAdapter);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<User>>> call, Throwable throwable) {
                Toast.makeText(getContext(), "Không gọi được API", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setControl(View view) {
        tvUserName = view.findViewById(R.id.tvUserName);
        spnUser = view.findViewById(R.id.spnUser);
        imgBtnSaveRole = view.findViewById(R.id.imgBtnSaveRole);

        rcvUser = view.findViewById(R.id.rcvUser);

    }

}