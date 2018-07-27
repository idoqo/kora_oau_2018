package xyz.mchl.ferapid.models;

import com.google.gson.annotations.SerializedName;

public class ResolveAccountResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private Data responseData;

    @SerializedName("code")
    private String statusCode;
    @SerializedName("message")
    private String statusMessage;

    public ResolveAccountResponse() {

    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getResponseData() {
        return responseData;
    }

    public void setResponseData(Data responseData) {
        this.responseData = responseData;
    }

    public static class Data
    {
        @SerializedName("account_name")
        private String accountName;

        public Data(String accountName) {
            this.accountName = accountName;
        }

        public Data() {

        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }
    }
}
