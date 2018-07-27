package xyz.mchl.ferapid;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.POST;
import xyz.mchl.ferapid.models.AuthRequest;
import xyz.mchl.ferapid.models.AuthToken;
import xyz.mchl.ferapid.models.ResolveAccountRequest;
import xyz.mchl.ferapid.models.ResolveAccountResponse;

public interface MoneywaveService
{
    @POST("/v1/merchant/verify")
    Call<AuthToken> fetchAuthToken(@Body AuthRequest authRequest);

    @POST("/v1/resolve/account")
    Call<ResolveAccountResponse> resolveAccount(@Body ResolveAccountRequest request);
}
