package xyz.mchl.ferapid.junk;

import android.util.Log;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.mchl.ferapid.MoneywaveService;
import xyz.mchl.ferapid.models.AuthRequest;
import xyz.mchl.ferapid.models.AuthToken;

public class Utils
{
    //todo terrible practice, move to config and REMOVE FROM REPO
    public final static String MWV_API_KEY = "ts_VXOAU0Q9GSQUVSXPUKH0";
    public final static String MWV_API_SECRET = "ts_2LAAFHL6QPA5R9ULCWWFQW0YVO1OXL";

    private String authToken;

    public static HashMap<String, String> fetchBankList() {
        HashMap<String, String> bankList = new HashMap<>();
        bankList.put("057", "ZENITH BANK PLC");
        bankList.put("035", "WEMA BANK PLC");
        bankList.put("033", "UNITED BANK FOR AFRICA PLC");
        bankList.put("032", "UNION BANK OF NIGERIA PLC");
        bankList.put("068", "STANDARD CHARTERED BANK NIGERIA LIMITED");
        bankList.put("076", "SKYE BANK PLC");
        bankList.put("082", "KEYSTONE BANK PLC");
        bankList.put("030", "HERITAGE BANK");
        bankList.put("058", "GTBANK PLC");
        bankList.put("214", "FCMB PLC");
        bankList.put("011", "UNITY BANK PLC");
        bankList.put("221", "STANBIC IBTC BANK PLC");
        bankList.put("304", "STERLING BANK PLC");
        bankList.put("044", "ACCESS BANK PLC");

        return bankList;
    }
}
