package com.example.hancafe.Controller.Api;

import com.example.hancafe.Model.CartData;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.Model.Product;
import com.example.hancafe.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // http://localhost:8888/api/category/list
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.1.4:8888/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    //Danh sách User
    @GET("api/user/")
    Call<ApiResponse<List<User>>> getUsers();
    //Danh sách Staff
    @GET("api/staff/")
    Call<ApiResponse<List<User>>> getStaffs();
    //Danh sách Customer
    @GET("api/customer/")
    Call<ApiResponse<List<User>>> getCustomers();

    //Cập nhật quyền cho nhân viên
    @PATCH("api/staff/{id}")
    Call<ApiResponse<User>> updateRoleStaffs(@Path("id") String id,
                                             @Query("role") int role);
    //Cập nhật quyền cho khách hàng
    @PATCH("api/customer/{id}")
    Call<ApiResponse<User>> updateRoleCustomers(@Path("id") String id,
                                                @Query("role") int role);
    //Đăng kí
    @POST("api/user/register")
    Call<ApiResponse<User>> register(@Body User user);
    //Đăng nhập
    @POST("api/user/login")
    Call<ApiResponse<User>> login(@Body User user);
    //Thể loại sản phẩm
    //Lấy danh sách các thể loại
    @GET("api/category/")
    Call<ApiResponse<List<CategoryProduct>>> getCategoryList();
    //Thêm thể loại
    @Multipart
    @POST("api/category/")
    Call<ApiResponse<CategoryProduct>> addCategoryProduct(@Part("name")RequestBody name,
                                                          @Part MultipartBody.Part image,
                                                          @Part("status")RequestBody status);
    //Sửa loại sản phẩm
    @Multipart
    @PUT("api/category/{id}")
    Call<ApiResponse<CategoryProduct>> editCategoryProduct(@Path("id") String id,
                                                           @Part("name")RequestBody name,
                                                           @Part MultipartBody.Part image,
                                                           @Part("status")RequestBody status);
    //Xóa loại sản phẩm
    @PATCH("api/category/{id}")
    Call<ApiResponse<CategoryProduct>> deleteCategoryProduct(@Path("id") String id,
                                                             @Query("status") int status);
    //Sản phẩm
    //Lấy danh sách các sản phẩm
    @GET("api/product/")
    Call<ApiResponse<List<Product>>> getProductList();
    //Thêm sản phẩm
    @Multipart
    @POST("api/product/")
    Call<ApiResponse<Product>> addProduct(@Part("name")RequestBody name,
                                          @Part("price")RequestBody price,
                                          @Part("idCategory")RequestBody idCategory,
                                          @Part("description")RequestBody description,
                                          @Part("status")RequestBody status,
                                          @Part MultipartBody.Part image,
                                          @Part("quantity")RequestBody quantity);
    //Sửa sản phẩm
    @Multipart
    @PUT("api/product/{id}")
    Call<ApiResponse<Product>> editProduct(@Path("id") String id,
                                           @Part("name")RequestBody name,
                                           @Part("price")RequestBody price,
                                           @Part("idCategory")RequestBody idCategory,
                                           @Part("description")RequestBody description,
                                           @Part("status")RequestBody status,
                                           @Part MultipartBody.Part image,
                                           @Part("quantity")RequestBody quantity);
    //Xóa sản phẩm
    @PATCH("api/product/{id}")
    Call<ApiResponse<Product>> deleteProduct(@Path("id") String id,
                                             @Query("status") int status);

    //Giỏ hàng
    //get cart
    @Multipart
    @POST("api/cart/get")
    Call<ApiResponse<CartData>> getCart(@Body Map<String, String> userId);
    //Add to cart
    @Multipart
    @POST("api/cart/add")
    Call<ApiResponse<User>> addToCart(@Part("userId") String userId,
                                      @Part("itemId") String itemId);

}
