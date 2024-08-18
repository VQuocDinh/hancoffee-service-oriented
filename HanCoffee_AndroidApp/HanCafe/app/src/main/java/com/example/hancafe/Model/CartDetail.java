package com.example.hancafe.Model;

public class CartDetail {

    private int idCartItem, idSize, quantity;
    String idProduct, idCart;

    public CartDetail() {
    }

    public CartDetail(int idCartItem, String idCart, String idProduct, int idSize, int quantity) {
        this.idCartItem = idCartItem;
        this.idCart = idCart;
        this.idProduct = idProduct;
        this.idSize = idSize;
        this.quantity = quantity;
    }

    public String getIdCart() {
        return idCart;
    }

    public void setIdCart(String idCart) {
        this.idCart = idCart;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdCartItem() {
        return idCartItem;
    }

    public void setIdCartItem(int idCartItem) {
        this.idCartItem = idCartItem;
    }

    public int getIdSize() {
        return idSize;
    }

    public void setIdSize(int idSize) {
        this.idSize = idSize;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}