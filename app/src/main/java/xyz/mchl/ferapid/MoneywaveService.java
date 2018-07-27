package xyz.mchl.ferapid;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import xyz.mchl.ferapid.models.AuthRequest;
import xyz.mchl.ferapid.models.AuthToken;
import xyz.mchl.ferapid.models.DisburseRequest;
import xyz.mchl.ferapid.models.ResolveAccountRequest;
import xyz.mchl.ferapid.models.ResolveAccountResponse;

public interface MoneywaveService
{
    @POST("/v1/merchant/verify")
    Call<AuthToken> fetchAuthToken(@Body AuthRequest authRequest);

    @POST("/v1/resolve/account")
    Call<ResolveAccountResponse> resolveAccount(
            @Header("Authorization") String authToken, @Body ResolveAccountRequest request);

    @POST("/v1/disburse")
    Call<JsonObject> disburseToAccount(@Header("Authorization") String authToken,
                                        @Body DisburseRequest disburseRequest);
}
