package com.example.skripsi.Model.Promotion;

import java.math.BigInteger;

public class Discount {
    private BigInteger id;
    private Integer discountGiven;
    private Integer maxDiscount;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Integer getDiscountGiven() {
        return discountGiven;
    }

    public void setDiscountGiven(Integer discountGiven) {
        this.discountGiven = discountGiven;
    }

    public Integer getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(Integer maxDiscount) {
        this.maxDiscount = maxDiscount;
    }
}
