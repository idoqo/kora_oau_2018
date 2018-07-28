package xyz.mchl.ferapid;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.mchl.ferapid.junk.RetrofitInstance;
import xyz.mchl.ferapid.junk.Utils;
import xyz.mchl.ferapid.models.AuthRequest;
import xyz.mchl.ferapid.models.AuthToken;
import xyz.mchl.ferapid.models.DisburseRequest;
import xyz.mchl.ferapid.models.ResolveAccountRequest;
import xyz.mchl.ferapid.models.ResolveAccountResponse;

import static xyz.mchl.ferapid.junk.Utils.MWV_API_KEY;
import static xyz.mchl.ferapid.junk.Utils.MWV_API_SECRET;

public class ProcessorActivity extends AppCompatActivity {

    public static String EXTRA_URI_DATA = "barcode_uri";
    String barcode;

    private String authToken;

    ProgressBar infoLoadingBar;
    LinearLayout accountInfoLayout;

    TextView tvAccountName;
    TextView tvAccountNumber;
    TextView tvAmount;
    TextView tvBankName;
    Button proceedButton;

    String accountNumber;
    String bankCode;
    String amountToSend;

    private Uri qrUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processor);

        tvAccountName = findViewById(R.id.account_name);
        tvAccountNumber = findViewById(R.id.account_number);
        tvAmount = findViewById(R.id.amount);
        tvBankName = findViewById(R.id.bank_name);
        proceedButton = findViewById(R.id.button_proceed);
        infoLoadingBar =  findViewById(R.id.infoLoadingBar);
        accountInfoLayout = findViewById(R.id.account_details_layout);


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
        bankCode = qrUri.getQueryParameter(getResources()
                .getString(R.string.ferapid_uri_param_bank_code));
        accountNumber = qrUri.getQueryParameter(getResources()
                .getString(R.string.ferapid_uri_param_account_number));
        amountToSend = qrUri.getQueryParameter(getResources()
                .getString(R.string.ferapid_uri_param_amount));

        activateListener();
        getAuthToken();
    }

    private void resolveAccount(final String accountNumber, final String bankCode) {
        MoneywaveService service = RetrofitInstance.getInstance()
                .create(MoneywaveService.class);
        ResolveAccountRequest resolveReq = new ResolveAccountRequest(accountNumber, bankCode);
        Log.d("ProcessorActivityToken", authToken);
        Call<ResolveAccountResponse> call = service.resolveAccount(authToken, resolveReq);
        call.enqueue(new Callback<ResolveAccountResponse>() {
            @Override
            public void onResponse(Call<ResolveAccountResponse> call, Response<ResolveAccountResponse> response) {
                infoLoadingBar.setVisibility(View.GONE);
                if (response.body() == null) {
                    try {
                        Log.d("ProcessorActivity", response.errorBody().string());
                    } catch (IOException ioe) {
                        Log.d("processorActivity", ioe.getMessage());
                    }
                    Toast.makeText(ProcessorActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT)
                            .show();
                    finish();
                } else {
                    if (response.body().getStatus().equals("success")) {
                        ResolveAccountResponse.Data data = response.body().getResponseData();
                        Log.d("ProcessorActivity", data.getAccountName());
                        tvAccountName.setText(data.getAccountName());
                        tvAccountNumber.setText(accountNumber);
                        tvAmount.setText("₦"+amountToSend);
                        tvBankName.setText(Utils.fetchBankList().get(bankCode));
                        accountInfoLayout.setVisibility(View.VISIBLE);
                    } else {
                        tvAccountName.setText("Failed to get data");
                        Log.d("ProcessorActivity", response.body().getStatusMessage());
                        Toast.makeText(ProcessorActivity.this, "status is error", Toast.LENGTH_SHORT)
                                .show();
                        finish();
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
        final AlertDialog.Builder dialogBuilder = createTransactionDialog();
        final AlertDialog transactionDialog = dialogBuilder.show();
        DisburseRequest disburseRequest = new DisburseRequest();
        disburseRequest.setWalletLock(walletLock);
        disburseRequest.setAccountNumber(accountNumber);
        disburseRequest.setAmountToSend(amount);
        disburseRequest.setBankCode(bankCode);
        disburseRequest.setSenderName("Test User");
        disburseRequest.setCurrency("NGN");
        disburseRequest.setReferenceCode(UUID.randomUUID()
                .toString().replace("-", "_"));
        MoneywaveService moneywaveService = RetrofitInstance.getInstance()
                .create(MoneywaveService.class);
        Call<JsonObject> call = moneywaveService.disburseToAccount(authToken, disburseRequest);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                transactionDialog.findViewById(R.id.processingProgressBar)
                        .setVisibility(View.GONE);
                ImageView statusView = transactionDialog.findViewById(R.id.transaction_status_icon);
                Bitmap icon = null;
                String transactionMsgTitle = "";
                String transactionMsgContent = "";

                if (response.body() == null) {
                    icon = BitmapFactory.decodeResource(ProcessorActivity.this.getResources(),
                            R.drawable.ic_wrong_circle);
                    try {
                        JsonObject jsonObject = new JsonParser()
                                .parse(response.errorBody().string())
                                .getAsJsonObject();
                        transactionMsgTitle = jsonObject.get("code").toString();
                        transactionMsgContent = jsonObject.get("message").toString();
                    } catch (IOException ioe) {
                        transactionMsgTitle = "Failed to complete request";
                        transactionMsgContent = ioe.getMessage();
                    }

                } else {
                    JsonObject responseObject = (JsonObject) response.body();
                    String status = responseObject.get("status").toString();
                    JsonObject stubData = responseObject.getAsJsonObject("data");
                    JsonObject actualData = responseObject.getAsJsonObject("data");

                    if (status.equals("\"success\"")) {
                        icon = BitmapFactory.decodeResource(ProcessorActivity.this.getResources(),
                                R.drawable.ic_check_circle);
                        transactionMsgTitle = "Your transaction was successful.";
                        transactionMsgContent = "Your transaction has been processed with reference: ABCDE";
                    } else {

                        icon = BitmapFactory.decodeResource(ProcessorActivity.this.getResources(),
                                R.drawable.ic_wrong_circle);
                        transactionMsgTitle = "Failed to process transaction.";
                        transactionMsgContent = "Loh nahhh";
                    }
                }
                TextView msgTV = transactionDialog.findViewById(R.id.transaction_msg_title);
                TextView contentTV = transactionDialog.findViewById(R.id.transaction_msg_content);
                statusView.setImageBitmap(icon);
                statusView.setVisibility(View.VISIBLE);
                msgTV.setText(transactionMsgTitle);
                contentTV.setText(transactionMsgContent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void activateListener() {
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDialog(view);
            }
        });
    }

    private AlertDialog.Builder createTransactionDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_transaction_success, null);
        dialogBuilder.setView(dialogView)
            .create();
        return dialogBuilder;
    }
}
