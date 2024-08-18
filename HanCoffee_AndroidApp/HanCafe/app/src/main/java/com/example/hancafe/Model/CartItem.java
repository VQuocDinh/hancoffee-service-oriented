package com.example.hancafe.Model;

import java.io.Serializable;

public class CartItem implements Serializable {
    int quantity, sizeId, idCartItem, productPrice;
    String productName, productImg, productId ;
    boolean isChecked;

    public CartItem(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public CartItem() {
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getIdCartItem() {
        return idCartItem;
    }

    public void setIdCartItem(int idCartItem) {
        this.idCartItem = idCartItem;
    }

    public CartItem(String productId, int quantity, int sizeId, int idCartItem, int productPrice, String productName, String productImg) {
        this.productId = productId;
        this.quantity = quantity;
        this.sizeId = sizeId;
        this.idCartItem = idCartItem;
        this.productPrice = productPrice;
        this.productName = productName;
        this.productImg = productImg;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }
}
