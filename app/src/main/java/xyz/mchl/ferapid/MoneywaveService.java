package xyz.mchl.ferapid;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.POST;
import xyz.mchl.ferapid.models.AuthRequest;
import xyz.mchl.ferapid.models.AuthToken;

public interface MoneywaveService
{
    @POST("/v1/merchant/verify")
    Call<AuthToken> fetchAuthToken(@Body AuthRequest authRequest, Callback<AuthToken> callback);
}
