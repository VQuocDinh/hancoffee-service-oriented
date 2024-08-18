package com.example.hancafe.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class OrderDetail implements Parcelable {
    String idOrder, imgProduct, nameProduct, idOrderDetail;
    int idSize, quantity, priceProduct;

    public OrderDetail(String idOrderDetail, String idOrder, String imgProduct, String nameProduct, int idSize, int quantity, int priceProduct) {
        this.idOrder = idOrder;
        this.imgProduct = imgProduct;
        this.nameProduct = nameProduct;
        this.idSize = idSize;
        this.quantity = quantity;
        this.priceProduct = priceProduct;
        this.idOrderDetail = idOrderDetail;
    }

    protected OrderDetail(Parcel in) {
        idOrder = in.readString();
        imgProduct = in.readString();
        nameProduct = in.readString();
        idOrderDetail = in.readString();
        idSize = in.readInt();
        quantity = in.readInt();
        priceProduct = in.readInt();
    }

    public static final Creator<OrderDetail> CREATOR = new Creator<OrderDetail>() {
        @Override
        public OrderDetail createFromParcel(Parcel in) {
            return new OrderDetail(in);
        }

        @Override
        public OrderDetail[] newArray(int size) {
            return new OrderDetail[size];
        }
    };

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(String imgProduct) {
        this.imgProduct = imgProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
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

    public int getPriceProduct() {
        return priceProduct;
    }

    public String getIdOrderDetail() {
        return idOrderDetail;
    }

    public void setIdOrderDetail(String idOrderDetail) {
        this.idOrderDetail = idOrderDetail;
    }

    public void setPriceProduct(int priceProduct) {
        this.priceProduct = priceProduct;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(idOrder);
        dest.writeString(imgProduct);
        dest.writeString(nameProduct);
        dest.writeString(idOrderDetail);
        dest.writeInt(idSize);
        dest.writeInt(quantity);
        dest.writeInt(priceProduct);
    }
}
