package com.example.hancafe.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class CategoryOrderManagement implements Serializable {
    DatabaseReference categoryOrderManagementId = FirebaseDatabase.getInstance().getReference("Category_Order_Management");

    private int idCategory;
    private String nameCategory;

    public CategoryOrderManagement() {
    }

    public CategoryOrderManagement(int idCategory, String nameCategory) {
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }
}
