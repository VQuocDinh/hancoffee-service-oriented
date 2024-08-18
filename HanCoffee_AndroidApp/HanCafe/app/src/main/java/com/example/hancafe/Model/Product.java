package com.example.hancafe.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable, Parcelable {
    @SerializedName("image")
    private String purl;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String describe;
    @SerializedName("_id")
    private String id;
    @SerializedName("idCategory")
    private String idCategory;
    @SerializedName("status")
    private int status;
    @SerializedName("price")
    private int price;
    private int quantity;


    public Product() {
    }

    public Product(String purl, String name, String describe, String id, int status, int price, int quantity) {
        this.purl = purl;
        this.name = name;
        this.describe = describe;
        this.id = id;
        this.status = status;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(String purl, String name, String describe, String id, int status, int price, String idCategory, int quantity) {
        this.purl = purl;
        this.name = name;
        this.describe = describe;
        this.id = id;
        this.status = status;
        this.price = price;
        this.idCategory = idCategory;
        this.quantity = quantity;
    }

    protected Product(Parcel in) {
        purl = in.readString();
        name = in.readString();
        describe = in.readString();
        id = in.readString();
        idCategory = in.readString();
        status = in.readInt();
        price = in.readInt();
        quantity = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(purl);
        dest.writeString(name);
        dest.writeString(describe);
        dest.writeString(id);
        dest.writeInt(status);
        dest.writeInt(price);
        dest.writeString(idCategory);
        dest.writeInt(quantity);
    }
}