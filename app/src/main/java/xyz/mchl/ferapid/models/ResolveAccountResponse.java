package xyz.mchl.ferapid.models;

import com.google.gson.annotations.SerializedName;

public class ResolveAccountResponse {
    @SerializedName("account_name")
    private String status;
    @SerializedName("data")
    private Data responseData;

    public ResolveAccountResponse() {

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
