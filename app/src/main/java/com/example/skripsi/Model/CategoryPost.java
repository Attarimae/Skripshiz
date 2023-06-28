package com.example.skripsi.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryPost  implements Serializable {

    @SerializedName(value="category_name")
    private String categoryName;

    public CategoryPost(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
