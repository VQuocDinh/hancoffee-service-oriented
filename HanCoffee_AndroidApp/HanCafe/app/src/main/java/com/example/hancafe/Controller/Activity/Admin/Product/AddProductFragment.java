package com.example.hancafe.Controller.Activity.Admin.Product;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hancafe.Controller.Activity.Adapter.ProductAdapter;
import com.example.hancafe.Controller.Api.ApiResponse;
import com.example.hancafe.Controller.Api.ApiService;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.Model.Product;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

public class AddProductFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText name,price,describe, quantity;
    Spinner spCategory;
    ArrayList<String> categoryList = new ArrayList<>();
    ArrayAdapter<String> spinnerAdapter;
    ArrayList<CategoryProduct> data = new ArrayList<>();
    Button btnSave,btnBack, btnChooseImage;
    Uri imageUri;
    ImageView imageView;
    Bitmap bitmap;
    FirebaseStorage storage;
    String imageUrl;
    StorageReference storageReference;
    ApiService apiService = ApiService.apiService;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_admin_product_add, container, false);

        name = view.findViewById(R.id.edtName);

        price = view.findViewById(R.id.edtPrice);
        describe = view.findViewById(R.id.edtDescribe);
        quantity = view.findViewById(R.id.edtQuantity);
        imageView = view.findViewById(R.id.imgPreview);
        btnSave= view.findViewById(R.id.btnSave);
        btnBack = view.findViewById(R.id.btnBack);
        btnChooseImage = view.findViewById(R.id.btnChooseImage);
        spCategory = view.findViewById(R.id.spCategory);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoryList);
        spCategory.setAdapter(spinnerAdapter);

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickImageFromGallery();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        loadCategoriesFromApi();

        return view;
    }

    private void loadCategoriesFromApi() {
        // Gọi API để lấy danh sách các danh mục sản phẩm
        Call<ApiResponse<List<CategoryProduct>>> call = apiService.getCategoryList();
        call.enqueue(new Callback<ApiResponse<List<CategoryProduct>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CategoryProduct>>> call, Response<ApiResponse<List<CategoryProduct>>> response) {
                if (response.isSuccessful()) {
                    List<CategoryProduct> categoryProducts = response.body().getData();
                    if (categoryProducts != null && !categoryProducts.isEmpty()) {
                        categoryList.clear();
                        data.clear();
                        for (CategoryProduct categoryProduct : categoryProducts) {
                            // Lấy tên của từng danh mục sản phẩm và thêm vào danh sách hiển thị trên spinner
                            categoryList.add(categoryProduct.getName());
                        }
                        data.addAll(categoryProducts);

                        // Cập nhật adapter của spinner
                        spinnerAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Không có danh mục sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể tải danh mục sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CategoryProduct>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                        imageView.setImageURI(imageUri);
                    }
                }else{
                    Toast.makeText(getContext(), "Vui lòng chọn hình ảnh", Toast.LENGTH_LONG).show();
                }
            }
    );
    private void uploadImage() {
        String productName = name.getText().toString();
        int productPrice = Integer.parseInt(price.getText().toString());
        String productDescription = describe.getText().toString();
        String productCategoryName = spCategory.getSelectedItem().toString();

        int productQuantity = Integer.parseInt(quantity.getText().toString());

        // Kiểm tra nếu bất kỳ trường nào cũng không được để trống
        if (productName.isEmpty() || productCategoryName.isEmpty() || String.valueOf(productPrice).isEmpty() || productDescription.isEmpty() || String.valueOf(productQuantity).isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

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


            StorageReference ref = storageReference.child("imagesProduct/" + randomName);

            ref.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // getDownLoadUrl to store in string
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (uri != null) {
                                imageUrl = uri.toString();
                                uploadProductInfo();

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

    private void uploadProductInfo() {
    // Lấy dữ liệu từ các trường nhập liệu
        String productName = name.getText().toString();
        int productPrice = Integer.parseInt(price.getText().toString());
        String productDescription = describe.getText().toString();
        int productQuantity = Integer.parseInt(quantity.getText().toString());
        String productCategoryName = spCategory.getSelectedItem().toString();

        // Kiểm tra xem các trường có rỗng không
        if (productName.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter name and image URL", Toast.LENGTH_SHORT).show();
            return;
        }

        String categoryId = "";
        for (CategoryProduct categoryProduct : data) {
            // Lấy tên của từng danh mục sản phẩm và thêm vào danh sách hiển thị trên spinner
            if(categoryProduct.getName().equals(productCategoryName)){
                categoryId = categoryProduct.getId();
            }
        }

        //Tạo request body cho từng trường dữ liệu
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), productName);
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(productPrice));
        RequestBody idCategory = RequestBody.create(MediaType.parse("text/plain"), categoryId);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), productDescription);
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(0));
        // Tạo MultipartBody.Part từ Uri của hình ảnh
        File file = new File(getRealPathFromURI(imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        RequestBody quantity = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(productQuantity));

        Call<ApiResponse<Product>> call = apiService.addProduct(name, price, idCategory, description, status, imagePart, quantity);
        call.enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Thêm loại thành công", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), "Thêm loại thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable throwable) {
                Toast.makeText(getContext(), "Lỗi: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
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