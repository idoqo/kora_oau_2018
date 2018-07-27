package xyz.mchl.ferapid.models;

import com.google.gson.annotations.SerializedName;

public class ResolveAccountRequest {
    @SerializedName("account_number")
    private String accountNumber;
    @SerializedName("bank_code")
    private String bankCode;

    public ResolveAccountRequest() {

    }

    public ResolveAccountRequest(String accountNumber, String bankCode) {
        this.accountNumber = accountNumber;
        this.bankCode = bankCode;
    }
}
