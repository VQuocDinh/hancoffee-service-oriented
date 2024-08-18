package com.example.hancafe.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryProduct implements Serializable, Parcelable {
    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String curl;
    @SerializedName("status")
    int status;

    public CategoryProduct() {
    }

    public CategoryProduct(String id, String name, String curl, int status) {
        this.id = id;
        this.name = name;
        this.curl = curl;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurl() {
        return curl;
    }

    public void setCurl(String curl) {
        this.curl = curl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    protected CategoryProduct(Parcel in) {
        name = in.readString();
        id = in.readString();
        curl = in.readString();
        status = in.readInt();
    }

    public static final Creator<CategoryProduct> CREATOR = new Creator<CategoryProduct>() {
        @Override
        public CategoryProduct createFromParcel(Parcel in) {
            return new CategoryProduct(in);
        }

        @Override
        public CategoryProduct[] newArray(int size) {
            return new CategoryProduct[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(curl);
        dest.writeInt(status);
    }
}