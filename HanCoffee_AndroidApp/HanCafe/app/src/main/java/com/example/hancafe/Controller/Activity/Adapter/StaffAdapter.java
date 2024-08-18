package com.example.hancafe.Controller.Activity.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Controller.Api.ApiResponse;
import com.example.hancafe.Controller.Api.ApiService;

import com.example.hancafe.Model.User;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {
    private List<User> list;
    private Context context;
    private List<String> roleList; // Danh sách vai trò từ Firebase
    private DatabaseReference mDatabase;
    private boolean showSpinnerForAdmin;

    public StaffAdapter(Context context, List<User> list, boolean showSpinnerForAdmin) {
        this.context = context;
        this.list = list;
        this.showSpinnerForAdmin = showSpinnerForAdmin;
        roleList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        fetchRolesFromFirebase();
    }

    private void fetchRolesFromFirebase() {

        ApiService apiService = ApiService.apiService;
        Call<ApiResponse<List<User>>> call = apiService.getUsers();
        call.enqueue(new Callback<ApiResponse<List<User>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<User>>> call, Response<ApiResponse<List<User>>> response) {
                if(response.isSuccessful()){
                    roleList.clear();
                    roleList.add("Admin");
                    roleList.add("Manager");
                    roleList.add("User");

                    for (User user: response.body().getData()){
                        if(user != null){
                            if(user.getId() != null){
                                switch (user.getRole()) {
                                    case 0:
                                        roleList.set(0, "Admin");
                                        break;
                                    case 1:
                                        roleList.set(1, "Manager");
                                        break;
                                    case 2:
                                        roleList.set(2, "User");
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<User>>> call, Throwable throwable) {

            }
        });
//        DatabaseReference usersRef = mDatabase.child("Users");
//        usersRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                roleList.clear();
//
//                roleList.add("Admin");
//                roleList.add("Manager");
//                roleList.add("User");
//                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//
//                    Staff staff = userSnapshot.getValue(Staff.class);
//
//                    if (staff != null) {
//                        if (staff.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                            // Nếu người dùng hiện tại có quyền được lưu trong Firebase
//                            // thì hiển thị quyền của người dùng đó trên Spinner
//                            switch (staff.getRole()) {
//                                case 0:
//                                    roleList.set(0, "Admin");
//                                    break;
//                                case 1:
//                                    roleList.set(1, "Manager");
//                                    break;
//                                case 2:
//                                    roleList.set(2, "User");
//                                    break;
//                                default:
//                                    break;
//                            }
//
//                        }
//                    }
//
//                }
//                notifyDataSetChanged(); // Thông báo rằng dữ liệu đã được cập nhật
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("FirebaseHelper", "Error fetching data", databaseError.toException());
//            }
//        });
    }


    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_staff_user_item, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        User staff = list.get(position);
        holder.tvUserName.setText(staff.getEmail());

        if(showSpinnerForAdmin && staff.getRole() == 1){
            holder.spnUser.setVisibility(View.VISIBLE);
            holder.tvUserName.setVisibility(View.VISIBLE);
            holder.imgBtnSaveRole.setVisibility(View.VISIBLE);

            // Thiết lập dữ liệu cho Spinner từ roleList
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_item, roleList);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spnUser.setAdapter(spinnerAdapter);

            // Lấy vai trò của người dùng hiện tại và thiết lập nó trong Spinner
            String userRole = getUserRole(staff);
            int roleIndex = roleList.indexOf(userRole);
            holder.spnUser.setSelection(roleIndex);
        } else {
            holder.spnUser.setVisibility(View.GONE);
            holder.tvUserName.setVisibility(View.GONE);
            holder.imgBtnSaveRole.setVisibility(View.GONE);
        }


        // Thiết lập người nghe cho sự kiện nhấn ImageButton
        holder.imgBtnSaveRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy vị trí của Spinner và giá trị đã chọn
                int spinnerPosition = holder.spnUser.getSelectedItemPosition();
                String selectedRole = roleList.get(spinnerPosition);
                // Xử lý khi nhấn ImageButton
                // Gửi dữ liệu về Firebase để cập nhật quyền của người dùng
                updateRoleInFirebase(staff, selectedRole);
            }
        });
    }
    private String getUserRole(User staff) {
        switch (staff.getRole()) {
            case 0:
                return "Admin";
            case 1:
                return "Manager";
            case 2:
                return "User";
            default:
                return "";
        }
    }
    private int convertRoleStringToInt(String selectedRoleString) {
        int selectedRoleInt = -1; // Giá trị mặc định nếu không tìm thấy vai trò phù hợp
        switch (selectedRoleString) {
            case "Admin":
                selectedRoleInt = 0;
                break;
            case "Manager":
                selectedRoleInt = 1;
                break;
            case "User":
                selectedRoleInt = 2;
                break;
            default:
                break;
        }
        return selectedRoleInt;
    }

    private void updateRoleInFirebase(User user, String selectedRole) {
        int selectedRoleInt = convertRoleStringToInt(selectedRole);
        String userId = user.getId();

        ApiService apiService = ApiService.apiService;
        Call<ApiResponse<User>> call = apiService.updateRoleStaffs(userId, selectedRoleInt);
        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if(response.isSuccessful()){
                    // Cập nhật vai trò của người dùng trong danh sách hiện tại
                    user.setRole(selectedRoleInt);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Cập nhật quyền thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Cập nhật quyền thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable throwable) {
                Toast.makeText(context, "Lỗi khi gọi API", Toast.LENGTH_SHORT).show();
            }
        });
//        int selectedRoleInt = convertRoleStringToInt(selectedRole);
//        DatabaseReference userRef = mDatabase.child("Users").child(emailStaff.getId()).child("role");
//        userRef.setValue(selectedRoleInt)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        // Xử lý thành công khi cập nhật quyền
//                        Toast.makeText(context, "Cập nhật quyền thành công", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Xử lý khi cập nhật quyền thất bại
//                        Toast.makeText(context, "Cập nhật quyền thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public static class StaffViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName;
        Spinner spnUser;
        ImageButton imgBtnSaveRole;
        LinearLayout lnStaff;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.tvUserName);
            spnUser = itemView.findViewById(R.id.spnUser);
            imgBtnSaveRole = itemView.findViewById(R.id.imgBtnSaveRole);
            lnStaff = itemView.findViewById(R.id.lnStaff);
        }
    }
}
