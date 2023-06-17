package com.example.skripsi.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryList implements Serializable {

    @SerializedName(value="id")
    private int id;
    @SerializedName(value="category_name")
    private String categoryName;

    public CategoryList(String categoryName,int id) {
        this.categoryName = categoryName;
        this.id = id;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
