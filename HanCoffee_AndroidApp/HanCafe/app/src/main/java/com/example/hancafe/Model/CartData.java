package com.example.hancafe.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class CartData {
    private boolean success;
    private Map<String, Integer> cartData;

    // Getters và setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Integer> getCartData() {
        return cartData;
    }

    public void setCartData(Map<String, Integer> cartData) {
        this.cartData = cartData;
    }

}
