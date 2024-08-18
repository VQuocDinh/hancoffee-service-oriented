package com.example.hancafe.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Staff {
    @SerializedName("_id")
    String id;
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;
    @SerializedName("name")
    String name;
    @SerializedName("createdAt")
    String date;
    @SerializedName("phone")
    String phone;
    @SerializedName("address")
    String address;
    @SerializedName("avatar")
    String imgAvt;
    @SerializedName("role")
    int role;
    @SerializedName("cartData")
    CartData cartData;
    public Staff() {
    }

    public Staff(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Staff(String id, String email, String password, String name, String date, String phone, String address, String imgAvt, int role,  CartData cartData) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.date = date;
        this.phone = phone;
        this.address = address;
        this.imgAvt = imgAvt;
        this.role = role;
        this.cartData = cartData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgAvt() {
        return imgAvt;
    }

    public void setImgAvt(String imgAvt) {
        this.imgAvt = imgAvt;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public CartData getCartData() {
        return cartData;
    }

    public void setCartData(CartData cartData) {
        this.cartData = cartData;
    }
}
