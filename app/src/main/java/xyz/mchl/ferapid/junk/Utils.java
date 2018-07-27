package xyz.mchl.ferapid.junk;

import android.util.Log;

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
}
