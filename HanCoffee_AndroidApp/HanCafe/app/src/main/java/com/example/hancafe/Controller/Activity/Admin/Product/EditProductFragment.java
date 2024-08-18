package com.example.hancafe.Controller.Activity.Admin.Product;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

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
import com.bumptech.glide.Glide;
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
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductFragment extends Fragment {

    private EditText edtName, edtPrice, edtDescribe, edtQuantity;
    private ImageView ivHinh;
    private Button btnChooseImage, btnUpdate;
    private Spinner spCategory;
    private ArrayList<String> categoryList = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<CategoryProduct> data = new ArrayList<>();
    private Product product;
    Uri imageUri;
    Uri oldImageUri;
    Bitmap bitmap;
    String imageUrl;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_product, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtName = view.findViewById(R.id.edtName);
        edtPrice = view.findViewById(R.id.edtPrice);
        edtDescribe = view.findViewById(R.id.edtDescribe);
        edtQuantity = view.findViewById(R.id.edtQuantity);
        ivHinh = view.findViewById(R.id.ivHinh);
        btnChooseImage = view.findViewById(R.id.btnChooseImage);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        spCategory = view.findViewById(R.id.spCategory);
        spCategory.setEnabled(false);
        spCategory.setClickable(false);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Khởi tạo ArrayAdapter và đặt nó cho Spinner
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoryList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(spinnerAdapter);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("product")) {
            product = bundle.getParcelable("product");
            if (product != null) {
                edtName.setText(product.getName());
                edtPrice.setText(String.valueOf(product.getPrice()));
                edtDescribe.setText(product.getDescribe());
                edtQuantity.setText(String.valueOf(product.getQuantity()));
                Glide.with(requireContext()).load(product.getPurl()).into(ivHinh);

                // Set category của sản phẩm lên đầu tiên trong Spinner
//                String categoryId = product.getIdCategory();
//                int categoryIndex = categoryList.indexOf(categoryId);
//                if (categoryIndex != -1) {
//                    spCategory.setSelection(categoryIndex);
//                }

            }
        }

        // Load categories from Api
        loadCategoriesFromApi();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    String name = edtName.getText().toString().trim();
                    int price = Integer.parseInt(edtPrice.getText().toString().trim());
                    String describe = edtDescribe.getText().toString().trim();
                    String categoryName = spCategory.getSelectedItem().toString();
                    int quantity = Integer.parseInt(edtQuantity.getText().toString().trim());

                    if (name.isEmpty() || categoryName.isEmpty() || describe.isEmpty() || String.valueOf(quantity).isEmpty()) {
                        Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Tạo đối tượng RequestBody cho các thông tin cần gửi lên server
                    String categoryId = "";
                    for (CategoryProduct categoryProduct : data) {
                        // Lấy tên của từng danh mục sản phẩm và thêm vào danh sách hiển thị trên spinner
                        if(categoryProduct.getName().equals(categoryName)){
                            categoryId = categoryProduct.getId();
                        }
                    }

                    String idProduct = product.getId();
                    RequestBody nameRequestBody = RequestBody.create(MediaType.parse("text/plain"), name);
                    RequestBody priceRequestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));
                    RequestBody idCategoryRequestBody = RequestBody.create(MediaType.parse("text/plain"), categoryId);
                    RequestBody describeRequestBody = RequestBody.create(MediaType.parse("text/plain"), describe);
                    RequestBody statusRequestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(0));
                    RequestBody quantityRequestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(quantity));

                    // Nếu người dùng chọn hình ảnh mới, tạo MultipartBody.Part cho hình ảnh mới.
                    // Nếu không, sử dụng ảnh cũ (oldImageUri)
                    MultipartBody.Part imagePart;
                    if (imageUri != null) {
                        File file = new File(getRealPathFromURI(imageUri));
                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
                        imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                    } else {
                        if (oldImageUri != null) {
                            File oldFile = new File(getRealPathFromURI(oldImageUri));
                            RequestBody oldRequestFile = RequestBody.create(MediaType.parse("image/jpeg"), oldFile);
                            imagePart = MultipartBody.Part.createFormData("image", oldFile.getName(), oldRequestFile);
                        } else {
                            // Trường hợp không có ảnh cũ và không chọn ảnh mới
                            // Bạn có thể xử lý tùy theo yêu cầu của ứng dụng
                            // ở đây mình sẽ gán imagePart là null để tránh lỗi
                            imagePart = null;
                        }
                    }
//                    // Nếu người dùng chọn hình ảnh mới, tạo MultipartBody.Part cho hình ảnh
//                    File file = new File(getRealPathFromURI(imageUri));
//                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
//                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                    // Gọi API để chỉnh sửa sản phẩm
                    ApiService.apiService.editProduct(idProduct, nameRequestBody, priceRequestBody, idCategoryRequestBody, describeRequestBody, statusRequestBody, imagePart, quantityRequestBody).enqueue(new Callback<ApiResponse<Product>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(requireContext(), "Chỉnh sửa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                                // Điều hướng về màn hình trước đó hoặc thực hiện các hành động khác sau khi chỉnh sửa thành công
                            } else {
                                Toast.makeText(requireContext(), "Lỗi khi chỉnh sửa sản phẩm: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                            Toast.makeText(requireContext(), "Lỗi khi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    uploadImage();
                }

            }
        });

        // Handle choose image button click
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
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
                                // Lưu imageUri vào biến tạm thời oldImageUri
                                oldImageUri = imageUri;
                                imageUri = data.getData();
                                ivHinh.setImageURI(imageUri);
                            }
                        }else{
                            Toast.makeText(getContext(), "Vui lòng chọn hình ảnh", Toast.LENGTH_LONG).show();
                        }
                    }
            );
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

    private void uploadImage() {
        if(imageUri!=null) {
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

    private void loadCategoriesFromApi() {
        // Gọi API để lấy danh sách các danh mục từ máy chủ và cập nhật danh sách trong spinner
        ApiService.apiService.getCategoryList().enqueue(new Callback<ApiResponse<List<CategoryProduct>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CategoryProduct>>> call, Response<ApiResponse<List<CategoryProduct>>> response) {
                if (response.isSuccessful()) {
                    List<CategoryProduct> categoryProducts = response.body().getData();
                    categoryList.clear();
                    data.clear();
                    for (CategoryProduct categoryProduct : categoryProducts) {
                        categoryList.add(categoryProduct.getName());

                        // Nếu categoryId của sản phẩm trùng với categoryId hiện tại, đặt category đó lên đầu tiên trong Spinner
                        if (product != null && product.getIdCategory().equals(categoryProduct.getId())) {
                            int selectedCategoryIndex = categoryList.indexOf(categoryProduct.getName());
                            if (selectedCategoryIndex != -1) {
                                spCategory.setSelection(selectedCategoryIndex);
                            }
                        }

                    }
                    data.addAll(categoryProducts);
                    spinnerAdapter.notifyDataSetChanged();



                } else {
                    Toast.makeText(requireContext(), "Lỗi khi tải danh mục: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CategoryProduct>>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi khi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}