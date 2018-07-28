package xyz.mchl.ferapid.persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class QRCode
{
    @PrimaryKey(autoGenerate = true)
    public int id;
    private int amount;
    public String accountNumber;
    public String bankCode;
    public String bankName;

    public QRCode(int amount, String accountNumber, String bankCode, String bankName) {
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.bankCode = bankCode;
        this.bankName = bankName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
