package com.example.hancafe.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.List;

public class OrderManagement implements Parcelable, Serializable {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Order_Management");

    int idCategory, price;
    String date, id, idUser;
    List<OrderDetail> orderDetails;
    private DatabaseReference categoryRef;


    public OrderManagement() {
    }

    public OrderManagement(int idCategory, int price, String date, String id, String idUser) {
        this.idCategory = idCategory;
        this.price = price;
        this.date = date;
        this.id = id;
        this.idUser = idUser;
        this.categoryRef = database.getReference("Category_Order_Management").child(String.valueOf(idCategory));
    }
    protected OrderManagement(Parcel in) {
        idCategory = in.readInt();
        price = in.readInt();
        date = in.readString();
        id = in.readString();
        idUser = in.readString();
        orderDetails = in.createTypedArrayList(OrderDetail.CREATOR);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String dateTime) {
        this.date = dateTime;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idCategory);
        dest.writeInt(price);
        dest.writeString(date);
        dest.writeString(id);
        dest.writeString(idUser);
        dest.writeTypedList(orderDetails);
    }
    public static final Creator<OrderManagement> CREATOR = new Creator<OrderManagement>() {
        @Override
        public OrderManagement createFromParcel(Parcel in) {
            return new OrderManagement(in);
        }

        @Override
        public OrderManagement[] newArray(int size) {
            return new OrderManagement[size];
        }
    };
}
