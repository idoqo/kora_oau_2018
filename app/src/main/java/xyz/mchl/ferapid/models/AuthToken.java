package xyz.mchl.ferapid.models;

import com.google.gson.annotations.SerializedName;

public class AuthToken {
    @SerializedName("status")
    private String responseStatus;
    @SerializedName("token")
    private String authToken;

    public AuthToken(String status, String token) {
        this.authToken = token;
        this.responseStatus = status;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
