package com.example.hancafe.Controller.Api;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private T data;
    @SerializedName("user")
    private T user;
    @SerializedName("cartData")
    private T cartData;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }

    public T getCartData() {
        return cartData;
    }

    public void setCartData(T cartData) {
        this.cartData = cartData;
    }
}
