package com.example.skripsi.Model.Promotion;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Condition;

public class PromotionItemModel {

    @SerializedName(value="id")
    private int id;
    @SerializedName(value="promotionName", alternate={"category","categoryName"})
    private String promotionName;
    private String promotionDescription;
    private Integer quota;

    private Date expiredDate;

    private String condition;

    private String promotionType = "DISCOUNT";

    @SerializedName(value = "watch_image_ads")
    private List<WatchImageAds> watchImageAds;

    @SerializedName(value = "social_media_ads")
    private List<SocialMediaAds> SocialMediaAds;

    @SerializedName(value = "post_social_media")
    private List<PostSocialMedia> postSocialMedia;
    private Discount discounts;

    public PromotionItemModel(int id, String promotionName, String promotionDescription, Integer quota, Date expiredDate, String condition, String promotionType, List<WatchImageAds> watchImageAds, List<com.example.skripsi.Model.Promotion.SocialMediaAds> socialMediaAds, List<PostSocialMedia> postSocialMedia, Discount discounts) {
        this.id = id;
        this.promotionName = promotionName;
        this.promotionDescription = promotionDescription;
        this.quota = quota;
        this.expiredDate = expiredDate;
        this.condition = condition;
        this.promotionType = promotionType;
        this.watchImageAds = watchImageAds;
        SocialMediaAds = socialMediaAds;
        this.postSocialMedia = postSocialMedia;
        this.discounts = discounts;
    }

    public PromotionItemModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionDescription() {
        return promotionDescription;
    }

    public void setPromotionDescription(String promotionDescription) {
        this.promotionDescription = promotionDescription;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public List<WatchImageAds> getWatchImageAds() {
        return watchImageAds;
    }

    public void setWatchImageAds(List<WatchImageAds> watchImageAds) {
        this.watchImageAds = watchImageAds;
    }

    public List<com.example.skripsi.Model.Promotion.SocialMediaAds> getSocialMediaAds() {
        return SocialMediaAds;
    }

    public void setSocialMediaAds(List<com.example.skripsi.Model.Promotion.SocialMediaAds> socialMediaAds) {
        SocialMediaAds = socialMediaAds;
    }

    public List<PostSocialMedia> getPostSocialMedia() {
        return postSocialMedia;
    }

    public void setPostSocialMedia(List<PostSocialMedia> postSocialMedia) {
        this.postSocialMedia = postSocialMedia;
    }

    public Discount getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Discount discounts) {
        this.discounts = discounts;
    }
}
