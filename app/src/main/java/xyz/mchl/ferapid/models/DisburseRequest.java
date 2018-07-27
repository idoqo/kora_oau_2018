package xyz.mchl.ferapid.models;

import com.google.gson.annotations.SerializedName;

public class DisburseRequest
{
    @SerializedName("lock")
    private String walletLock;
    @SerializedName("amount")
    private int amountToSend;
    @SerializedName("bankcode")
    private String bankCode;
    @SerializedName("accountNumber")
    private String accountNumber;
    @SerializedName("senderName")
    private String senderName;
    @SerializedName("ref")
    private String referenceCode;
    @SerializedName("currency")
    private String currency;

    public DisburseRequest(String walletLock, int amount, String bankCode, String accountNumber,
                           String senderName, String referenceCode, String currency) {
        this.walletLock = walletLock;
        this.amountToSend = amount;
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
        this.senderName = senderName;
        this.referenceCode = referenceCode;
        this.currency = currency;
    }

    public DisburseRequest() {

    }

    public String getWalletLock() {
        return walletLock;
    }

    public void setWalletLock(String walletLock) {
        this.walletLock = walletLock;
    }

    public int getAmountToSend() {
        return amountToSend;
    }

    public void setAmountToSend(int amountToSend) {
        this.amountToSend = amountToSend;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
