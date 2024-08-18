package com.example.hancafe.Controller.Activity.Admin;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.hancafe.Controller.Activity.Map.MapsActivity;
import com.example.hancafe.Controller.Activity.User.PersonInformation.MainPersonInformationActivity;
import com.example.hancafe.Controller.Api.ApiResponse;
import com.example.hancafe.Controller.Api.ApiService;
import com.example.hancafe.Controller.Log.FileLogger;
import com.example.hancafe.Model.Staff;
import com.example.hancafe.Model.User;
import com.example.hancafe.R;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformationPersonAdminFragment extends Fragment {
    EditText edtName, edtPhone, edtDate, edtAddress;
    TextView tvEdit, tvExit, tvEmail, textView3;
    Button btnSave;
    ImageView ivDate, imgMap;
    CircleImageView imgAvt;
    Uri imageUri;
    String imageUrl;
    Bitmap bitmap;
    DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("Users");;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private static final int MAP_REQUEST_CODE = 100;
    private static final String PREF_LATITUDE = "latitude";
    private static final String PREF_LONGITUDE = "longitude";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_person_information, container, false);
//        setEvent();
//        setControl(view);
//        initData();
        return view;
    }

    private void initData() {

//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
//
//            // Thêm ValueEventListener để lấy dữ liệu từ Firebase
//            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        // Lấy dữ liệu từ dataSnapshot và hiển thị lên các EditText
//                        String name = dataSnapshot.child("name").getValue(String.class);
//                        String phone = dataSnapshot.child("phone").getValue(String.class);
//                        String date = dataSnapshot.child("date").getValue(String.class);
//                        String address = dataSnapshot.child("address").getValue(String.class);
//                        String imageAvt = dataSnapshot.child("imgAvt").getValue(String.class);
//
//                        edtName.setText(name);
//                        edtPhone.setText(phone);
//                        edtDate.setText(date);
//                        edtAddress.setText(address);
//
//                        Uri PhotoUrl = currentUser.getPhotoUrl();
//                        Glide.with(getContext()).load(imageAvt).error(R.drawable.avtdefault).into(imgAvt);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // Xử lý khi có lỗi xảy ra
//                    Log.e("InformationPrivate", "Firebase Database Error: " + databaseError.getMessage());
//                }
//            });
//        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ các EditText
                String name = edtName.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String date = edtDate.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();

//                Uri PhotoUrl = currentUser.getPhotoUrl();
//                Glide.with(getContext()).load(PhotoUrl).error(R.drawable.avtdefault).into(imgAvt);

                // Kiểm tra xem người dùng đã nhập đủ thông tin hay chưa
                if (name.isEmpty() || phone.isEmpty() || date.isEmpty() || address.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
//                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                if (currentUser != null) {
//                    String userId = currentUser.getUid();
//
//                    // Lưu dữ liệu vào Firebase Realtime Database
//                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
//                    userRef.child("name").setValue(name);
//                    userRef.child("phone").setValue(phone);
//                    userRef.child("date").setValue(date);
//                    userRef.child("address").setValue(address);
//                    userRef.child("imgAvt").setValue(PhotoUrl);
//
//                    // Hiển thị thông báo hoàn thành
//                    Toast.makeText(getContext(), "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
//                    edtName.clearFocus();
//                    edtPhone.clearFocus();
//                    edtDate.clearFocus();
//                    edtAddress.clearFocus();
//                    imgAvt.clearFocus();
//                }
                uploadImage();
            }

        });

        ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGGMap();
            }
        });
        imgAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });

    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        resultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent>
            resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        imageUri = data.getData();

                        imgAvt.setImageURI(imageUri);

                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Vui lòng chọn hình ảnh", Toast.LENGTH_LONG).show();
                }
            }
    );

    private void uploadImage() {

        if(imageUri!=null){
            final String randomName = UUID.randomUUID().toString();
            byte[] bytes = new byte[0];
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.requireActivity().getContentResolver(), imageUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                bytes = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

            StorageReference ref = storageReference.child("imagesAvatar/" + randomName);
            ref.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // getDownLoadUrl to store in string
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (uri != null) {
                                imageUrl = uri.toString();
                                uploadCategoryInfo();
                            }
                            imageUri = null;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void uploadCategoryInfo() {
        // Lấy ID của người dùng hiện tại
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Tạo một Map chỉ chứa trường "imgAvt" cần cập nhật
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("imgAvt", imageUrl);

        // Sử dụng ID của người dùng hiện tại làm khóa chính
        DatabaseReference userRef = categoryRef.child(userId);

        userRef.updateChildren(updateMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Thay đổi avatar thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Thay đổi avatar thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openGGMap() {

        String address = edtAddress.getText().toString().trim();
        if (!address.isEmpty()) {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocationName(address, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address location = addresses.get(0);
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    Intent intent = new Intent(getContext(), MapsActivity.class);
                    intent.putExtra(PREF_LATITUDE, latitude);
                    intent.putExtra(PREF_LONGITUDE, longitude);
                    startActivityForResult(intent, MAP_REQUEST_CODE);
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy địa chỉ", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Có lỗi xảy ra khi tìm kiếm địa chỉ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            double latitude = data.getDoubleExtra(PREF_LATITUDE, 0);
            double longitude = data.getDoubleExtra(PREF_LONGITUDE, 0);

            String address = getAddressFromLatLng(latitude, longitude);

            edtAddress.setText(address);
        }
    }

    private String getAddressFromLatLng(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        String address = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    sb.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                address = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    private void transactionFragment() {
        UserAdminFragment userAdminFragment = new UserAdminFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.container_admin, userAdminFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo một DatePickerDialog và hiển thị nó
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Hiển thị ngày được chọn trong ô edtDate
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        calendar.set(year, month, dayOfMonth);
                        String formattedDate = sdf.format(calendar.getTime());
                        edtDate.setText(formattedDate);
                    }
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void setControl(View view) {
        imgAvt = view.findViewById(R.id.imgAvt);
        tvEmail = view.findViewById(R.id.tvEmail);
        edtName = view.findViewById(R.id.edtName);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtDate = view.findViewById(R.id.edtDate);
        edtAddress = view.findViewById(R.id.edtAddress);
        tvEdit = view.findViewById(R.id.tvEdit);
        tvEdit.setVisibility(View.GONE);
        tvExit = view.findViewById(R.id.tvExit);
        tvExit.setVisibility(View.GONE);
        btnSave = view.findViewById(R.id.btnSave);
        ivDate = view.findViewById(R.id.ivDate);
        textView3 = view.findViewById(R.id.textView3);
        textView3.setVisibility(View.GONE);
        imgMap = view.findViewById(R.id.imgMap);
    }

    private void setEvent() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                tvEmail.setText(user.getEmail());
                Uri PhotoUrl = currentUser.getPhotoUrl();
                Glide.with(getContext()).load(PhotoUrl).error(R.drawable.avtdefault).into(imgAvt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}