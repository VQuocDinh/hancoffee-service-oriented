package com.example.hancafe.Controller.Activity.Admin.Product;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import com.example.hancafe.Controller.Activity.Adapter.CategoryProductAdapter;
import com.example.hancafe.Controller.Api.ApiResponse;
import com.example.hancafe.Controller.Api.ApiService;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoryProductFragment extends Fragment{
    CategoryProductAdapter categoryProductAdapter;
    Bitmap bitmap;
    List<CategoryProduct> list;
    private EditText catName;
    private Button btnSave, btnBack, btnChooseImage;
    private DatabaseReference categoryRef;
    private ImageView imgPreview;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    Uri imageUri;
    String imageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_category_product_add, container, false);

        // Ánh xạ các view từ layout XML
        catName = view.findViewById(R.id.edtName);
        imgPreview = view.findViewById(R.id.imgPreview);
        btnSave = view.findViewById(R.id.btnSave);
        btnBack = view.findViewById(R.id.btnBack);
        btnChooseImage = view.findViewById(R.id.btnChooseImage);

//        // Lấy tham chiếu đến node "category" trong Firebase Database
//        categoryRef = FirebaseDatabase.getInstance().getReference().child("Category_Products");

        categoryProductAdapter = new CategoryProductAdapter(getContext(),list);
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });
        // Thêm sự kiện click cho nút lưu
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();
            }
        });

        // Thêm sự kiện click cho nút quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kết thúc Fragment và quay lại Fragment hoặc Activity trước đó
                getActivity().onBackPressed();
            }
        });

        return view;
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

                        imgPreview.setImageURI(imageUri);

                    }
                }else{
                    Toast.makeText(getContext(), "Vui lòng chọn hình ảnh", Toast.LENGTH_LONG).show();
                }
            }
    );

    private void uploadImage() {
        Boolean canNext = true;

        if (catName.getText().toString().trim().equals("")) {
            catName.setError("Vui lòng nhập tên danh mục!");
            catName.setFocusable(true);
            canNext = false;
        }
        if (canNext) {
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

                StorageReference ref = storageReference.child("imagesCategory/" + randomName);
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
                                //Toast.makeText(ProductManagement.this, "Uploaded", Toast.LENGTH_SHORT).show();
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

                        //Toast.makeText(ProductManagement.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }
    private void uploadCategoryInfo() {
        // Lấy dữ liệu từ các trường nhập liệu
        String name = catName.getText().toString().trim();

        // Kiểm tra xem các trường có rỗng không
        if (name.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter name and image URL", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiService.apiService;

        // Tạo RequestBody cho các trường dữ liệu
        RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), name);

        // Tạo MultipartBody.Part từ Uri của hình ảnh
        File file = new File(getRealPathFromURI(imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        RequestBody statusPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(0));

        Call<ApiResponse<CategoryProduct>> call = apiService.addCategoryProduct(namePart, imagePart, statusPart);

        call.enqueue(new Callback<ApiResponse<CategoryProduct>>() {
            @Override
            public void onResponse(Call<ApiResponse<CategoryProduct>> call, Response<ApiResponse<CategoryProduct>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Thêm loại thành công", Toast.LENGTH_SHORT).show();
                    categoryProductAdapter.notifyDataSetChanged();
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), "Thêm loại thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CategoryProduct>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRealPathFromURI(Uri imageUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getContext(), imageUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}