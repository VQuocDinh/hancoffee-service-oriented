package com.example.hancafe.Model;

public class CTHD {
    private String nameProduct;
    private int quantity, priceProduct;

    public CTHD() {
    }

    public CTHD(String nameProduct, int quantity, int priceProduct) {
        this.quantity = quantity;
        this.priceProduct = priceProduct;
        this.nameProduct = nameProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(int priceProduct) {
        this.priceProduct = priceProduct;
    }
}
