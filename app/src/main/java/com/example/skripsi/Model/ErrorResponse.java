package com.example.skripsi.Model;

import com.google.gson.annotations.JsonAdapter;

@JsonAdapter(ErrorResponseDeserializer.class)
public class ErrorResponse {
    private int status;
    private String response_message;

    public int getStatus() {
        return status;
    }

    public String getResponseMessage() {
        return response_message;
    }

    public ErrorResponse(int status, String response_message) {
        this.status = status;
        this.response_message = response_message;
    }

}
