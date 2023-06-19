package com.example.skripsi.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigInteger;

public class FullProfile implements Serializable {
    @SerializedName(value="id")
    private int id;

    @SerializedName(value = "motto")
    private String motto;

    @SerializedName(value = "motto_detail")
    private String mottoDetail;

    @SerializedName(value = "restaurant_story")
    private String restaurantStory;

    @SerializedName(value = "open_day")
    private String openDay;

    @SerializedName(value = "open_time")
    private String openTime;

    @SerializedName(value = "location")
    private String Location;

    @SerializedName(value = "official_email")
    private String officialEmail;

    @SerializedName(value = "facebook_url")
    private String facebookUrl;

    @SerializedName(value = "instagram_url")
    private String instagramUrl;

    @SerializedName(value = "whatsapp_number")
    private String whatsappNumber;

    public FullProfile(int id, String motto, String mottoDetail, String restaurantStory, String openDay, String openTime, String location, String officialEmail, String facebookUrl, String instagramUrl, String whatsappNumber) {
        this.id = id;
        this.motto = motto;
        this.mottoDetail = mottoDetail;
        this.restaurantStory = restaurantStory;
        this.openDay = openDay;
        this.openTime = openTime;
        Location = location;
        this.officialEmail = officialEmail;
        this.facebookUrl = facebookUrl;
        this.instagramUrl = instagramUrl;
        this.whatsappNumber = whatsappNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getMottoDetail() {
        return mottoDetail;
    }

    public void setMottoDetail(String mottoDetail) {
        this.mottoDetail = mottoDetail;
    }

    public String getRestaurantStory() {
        return restaurantStory;
    }

    public void setRestaurantStory(String restaurantStory) {
        this.restaurantStory = restaurantStory;
    }

    public String getOpenDay() {
        return openDay;
    }

    public void setOpenDay(String openDay) {
        this.openDay = openDay;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getOfficialEmail() {
        return officialEmail;
    }

    public void setOfficialEmail(String officialEmail) {
        this.officialEmail = officialEmail;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }
}
