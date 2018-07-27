package xyz.mchl.ferapid.models;

import com.google.gson.annotations.SerializedName;

public class ResolveAccountResponse {
    @SerializedName("account_name")
    private String accountName;

    public ResolveAccountResponse() {

    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
