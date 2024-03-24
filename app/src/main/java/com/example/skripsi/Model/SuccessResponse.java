package com.example.skripsi.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SuccessResponse implements Serializable {
    int status;

    @SerializedName(value = "response_message")
    String responseMessage;
}
