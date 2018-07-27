package xyz.mchl.ferapid;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

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

    private String authToken;

    TextView tvAccountName;
    TextView tvAccountNumber;
    TextView tvAmount;
    TextView tvBankName;

    Button proceedButton;

    private Uri qrUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processor);

        tvAccountName = findViewById(R.id.account_name);
        tvAccountNumber = findViewById(R.id.account_number);
        tvAmount = findViewById(R.id.amount);
        tvBankName = findViewById(R.id.bank_name);
        proceedButton = (Button) findViewById(R.id.button_proceed);

        barcode = getIntent().getStringExtra(EXTRA_URI_DATA);
        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(this, "No data to process", Toast.LENGTH_SHORT).show();
            finish();
        }
        qrUri = Uri.parse(barcode);
        String scheme = getResources().getString(R.string.ferapid_uri_scheme);
        if (!qrUri.getScheme().equals(scheme)) {
            Toast.makeText(this, "Failed to process data", Toast.LENGTH_SHORT).show();
            finish();
        }
        String bankCode = qrUri.getQueryParameter(getResources()
                .getString(R.string.ferapid_uri_param_bank_code));
        String accountNumber = qrUri.getQueryParameter(getResources()
                .getString(R.string.ferapid_uri_param_account_number));
        String amount = qrUri.getQueryParameter(getResources()
                .getString(R.string.ferapid_uri_param_amount));

//        tvAccountNumber.setText(accountNumber);
        //tvAmount.setText(amount);
        activateListener();
        getAuthToken();
    }

    private void resolveAccount(final String accountNumber, String bankCode) {
        MoneywaveService service = RetrofitInstance.getInstance()
                .create(MoneywaveService.class);
        ResolveAccountRequest resolveReq = new ResolveAccountRequest(accountNumber, bankCode);
        Log.d("ProcessorActivityToken", authToken);
        Call<ResolveAccountResponse> call = service.resolveAccount(authToken, resolveReq);
        call.enqueue(new Callback<ResolveAccountResponse>() {
            @Override
            public void onResponse(Call<ResolveAccountResponse> call, Response<ResolveAccountResponse> response) {
                if (response.body() == null) {
                    try {
                        Log.d("ProcessorActivity", response.errorBody().string());
                    } catch (IOException ioe) {
                        Log.d("processorActivity", ioe.getMessage());
                    }
                } else {
                    if (response.body().getStatus().equals("success")) {
                        ResolveAccountResponse.Data data = response.body().getResponseData();
                        Log.d("ProcessorActivity", data.getAccountName());
                        tvAccountName.setText(data.getAccountName());
                        tvBankName.setText(Utils.fetchBankList().get("058"));
                    } else {
                        tvAccountName.setText("Failed to get data");
                        Log.d("ProcessorActivity", response.body().getStatusMessage());
                        Toast.makeText(ProcessorActivity.this, "status is error", Toast.LENGTH_SHORT)
                                .show();
                    }
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
                String bankCode = qrUri.getQueryParameter(getResources()
                        .getString(R.string.ferapid_uri_param_bank_code));
                String accountNumber = qrUri.getQueryParameter(getResources()
                        .getString(R.string.ferapid_uri_param_account_number));
                resolveAccount(accountNumber, bankCode);
            }
            @Override
            public void onFailure(Call<AuthToken> call, Throwable t) {

            }
        });
    }

    private void showConfirmDialog(View parentView) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_confirm_payment, null);
        dialogBuilder.setView(dialogView)
                //add action buttons
                .setPositiveButton(R.string.confirm_payment, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView amountTV = dialogView.findViewById(R.id.amount_to_send);
                        TextView walletLockTV = dialogView.findViewById(R.id.moneywave_lock);
                        int amount = (amountTV.getText().toString().isEmpty()) ? 0 :
                                Integer.parseInt(amountTV.getText().toString());
                        String walletLock = walletLockTV.getText().toString();
                        processPayment(amount, walletLock);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void processPayment(int amount, String walletLock) {
        Log.d("WalletLock", walletLock);
    }

    private void activateListener() {
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDialog(view);
            }
        });
    }
}
