package com.example.skripsi.Model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ErrorResponseDeserializer implements JsonDeserializer<ErrorResponse> {
    @Override
    public ErrorResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int status = jsonObject.get("status").getAsInt();
        String responseMessage = jsonObject.get("response_message").getAsString();
        return new ErrorResponse(status, responseMessage);
    }
}
