package xyz.mchl.ferapid.models;

import com.google.gson.annotations.SerializedName;

public class AuthRequest {
    @SerializedName("apiKey")
    private String apiKey;
    @SerializedName("secret")
    private String secret;

    public AuthRequest() {
        this.apiKey = null;
        this.secret = null;
    }

    public AuthRequest(String key, String secret) {
        this.apiKey = key;
        this.secret = secret;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
