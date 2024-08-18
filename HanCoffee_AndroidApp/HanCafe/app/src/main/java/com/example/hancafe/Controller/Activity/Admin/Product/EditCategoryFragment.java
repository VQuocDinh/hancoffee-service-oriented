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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hancafe.Controller.Api.ApiResponse;
import com.example.hancafe.Controller.Api.ApiService;
import com.example.hancafe.Model.CategoryProduct;
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
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCategoryFragment extends Fragment {

    EditText edtName;
    ImageView ivHinh;
    Button btnChooseImage, btnUpdate;
    Uri imageUri;
    Uri oldImageUri;
    Bitmap bitmap;
    String imageUrl;
    CategoryProduct categoryProduct;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_category, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtName = view.findViewById(R.id.edtName);
        ivHinh = view.findViewById(R.id.ivHinh);
        btnChooseImage = view.findViewById(R.id.btnChooseImage);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("category")) {
            categoryProduct = bundle.getParcelable("category");
            if (categoryProduct != null) {
                edtName.setText(categoryProduct.getName());
                Glide.with(requireContext()).load(categoryProduct.getCurl()).into(ivHinh);

            }
        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryProduct != null) {
                    String name = edtName.getText().toString().trim();

                    if (name.isEmpty()) {
                        Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String idCategory = categoryProduct.getId();
                    RequestBody nameReqBody = RequestBody.create(MediaType.parse("text/plain"), name);
                    RequestBody statusReqBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(0));
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
//                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
//                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                    ApiService apiService = ApiService.apiService;
                    Call<ApiResponse<CategoryProduct>> call = apiService.editCategoryProduct(idCategory, nameReqBody, imagePart, statusReqBody);
                    call.enqueue(new Callback<ApiResponse<CategoryProduct>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<CategoryProduct>> call, Response<ApiResponse<CategoryProduct>> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(requireContext(), "Chỉnh sửa loại sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<CategoryProduct>> call, Throwable throwable) {
                            Toast.makeText(requireContext(), "Lỗi khi gọi api: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    uploadImage();

//                    // Update product in Firebase
//                    DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("Category_Products").child(idCategory);
//                    categoryRef.child("name").setValue(name);
//                    // Update image URL if a new image is chosen
//                    if (imageUri != null) {
//                        FirebaseStorage storage = FirebaseStorage.getInstance();
//                        StorageReference storageRef = storage.getReference();
//                        StorageReference imageRef = storageRef.child("imagesCategory").child(idCategory);
//
//                        imageRef.putFile(imageUri)
//                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uri) {
//                                                String imageUrl = uri.toString();
//                                                categoryRef.child("curl").setValue(imageUrl)
//                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                            @Override
//                                                            public void onSuccess(Void aVoid) {
//                                                                // Update image in the product list
//                                                                categoryProduct.setCurl(imageUrl);
//
//                                                                Toast.makeText(requireContext(), "Chỉnh sửa danh mục thành công", Toast.LENGTH_SHORT).show();
//
//                                                                // Go back to previous fragment
//                                                                requireActivity().getSupportFragmentManager().popBackStack();
//
//                                                            }
//                                                        })
//                                                        .addOnFailureListener(new OnFailureListener() {
//                                                            @Override
//                                                            public void onFailure(@NonNull Exception e) {
//                                                                Toast.makeText(requireContext(), "Lỗi khi cập nhật URL ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        });
//                                            }
//                                        });
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(requireContext(), "Lỗi khi tải ảnh lên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                    } else {
//                        Toast.makeText(requireContext(), "Chỉnh sửa danh mục thành công", Toast.LENGTH_SHORT).show();
//
//                        // Go back to previous fragment
//                        requireActivity().getSupportFragmentManager().popBackStack();
//                    }
                }

            }
        });


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

    private String getRealPathFromURI(Uri imageUri) {
        if (imageUri == null) {
            return null;
        }
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