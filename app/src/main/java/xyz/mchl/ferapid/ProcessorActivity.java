package xyz.mchl.ferapid;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.mchl.ferapid.junk.RetrofitInstance;
import xyz.mchl.ferapid.junk.Utils;
import xyz.mchl.ferapid.models.AuthRequest;
import xyz.mchl.ferapid.models.AuthToken;
import xyz.mchl.ferapid.models.ResolveAccountRequest;
import xyz.mchl.ferapid.models.ResolveAccountResponse;

import static xyz.mchl.ferapid.junk.Utils.MWV_API_KEY;
import static xyz.mchl.ferapid.junk.Utils.MWV_API_SECRET;

public class ProcessorActivity extends AppCompatActivity {

    public static String EXTRA_URI_DATA = "barcode_uri";
    String barcode;

    public String authToken;

    TextView tvAccountName;
    TextView tvAccountNumber;
    TextView tvAmount;
    TextView tvBankName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processor);

        tvAccountName = findViewById(R.id.account_name);
        tvAccountNumber = findViewById(R.id.account_number);
        tvAmount = findViewById(R.id.amount);
        tvBankName = findViewById(R.id.bank_name);

        barcode = getIntent().getStringExtra(EXTRA_URI_DATA);

        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(this, "No data to process", Toast.LENGTH_SHORT).show();
            finish();
        }

        Uri qrUri = Uri.parse(barcode);
        String scheme = getResources().getString(R.string.ferapid_uri_scheme);
        if (!qrUri.getScheme().equals(scheme)) {
            Toast.makeText(this, "Failed to process data", Toast.LENGTH_SHORT).show();
            finish();
        }

        String bankCode = qrUri.getQueryParameter(getResources()
                .getString(R.string.ferapid_uri_param_bank_code));
        String accountNumber = qrUri.getQueryParameter(getResources()
                .getString(R.string.ferapid_uri_param_account_number));
        resolveAccount(accountNumber, bankCode);
    }

    private void resolveAccount(String accountNumber, String bankCode) {
        if (authToken == null) {
            getAuthToken();
        }
        MoneywaveService service = RetrofitInstance.getInstance()
                .create(MoneywaveService.class);
        ResolveAccountRequest resolveReq = new ResolveAccountRequest(accountNumber, bankCode);
        Call<ResolveAccountResponse> call = service.resolveAccount(resolveReq);
        call.enqueue(new Callback<ResolveAccountResponse>() {
            @Override
            public void onResponse(Call<ResolveAccountResponse> call, Response<ResolveAccountResponse> response) {
                if (response.body() != null && response.body().getStatus().equals("success")) {
                    ResolveAccountResponse.Data data = response.body().getResponseData();
                    Log.d("ProcessorActivity", data.getAccountName());
                    tvAccountName.setText(data.getAccountName());
                } else {
                    tvAccountName.setText("Failed to get data");
                }
            }

            @Override
            public void onFailure(Call<ResolveAccountResponse> call, Throwable t) {

            }
        });
    }

    private void getAuthToken() {
        AuthRequest request = new AuthRequest(MWV_API_KEY, MWV_API_SECRET);
        MoneywaveService moneywaveService = RetrofitInstance.getInstance()
                .create(MoneywaveService.class);
        Call<AuthToken> call = moneywaveService.fetchAuthToken(request);
        Log.wtf("URL Called", call.request().url()+"");
        call.enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                authToken = response.body().getAuthToken();
            }
            @Override
            public void onFailure(Call<AuthToken> call, Throwable t) {

            }
        });
    }
}
