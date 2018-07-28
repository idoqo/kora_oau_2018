package xyz.mchl.ferapid;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.glxn.qrgen.android.QRCode;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import xyz.mchl.ferapid.junk.Utils;

public class ReceiveActivity extends AppCompatActivity {

    Button buttonNewCode;

    private QrCodeListViewModel qrCodeViewModel;
    private QrCodeListAdapter recyclerAdapter;
    private RecyclerView qrRecyclerView;

    public static String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        TAG = getClass().getName();
        buttonNewCode = (Button) findViewById(R.id.new_code);

        qrRecyclerView = findViewById(R.id.qr_recycler_view);
        recyclerAdapter = new QrCodeListAdapter(this, new ArrayList<xyz.mchl.ferapid.persistence.QRCode>());
        qrRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        qrRecyclerView.setAdapter(recyclerAdapter);

        qrCodeViewModel = ViewModelProviders.of(this).get(QrCodeListViewModel.class);
        qrCodeViewModel.getQrCodeList().observe(ReceiveActivity.this, new Observer<List<xyz.mchl.ferapid.persistence.QRCode>>() {
            @Override
            public void onChanged(@Nullable List<xyz.mchl.ferapid.persistence.QRCode> qrCodes) {
                recyclerAdapter.addItems(qrCodes);
            }
        });

        activateListeners();
    }

    private void activateListeners() {
        buttonNewCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGenerateDialog(view);
            }
        });
    }

    private void showGenerateDialog(View parentView) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_generate_code, null);
        dialogBuilder.setView(dialogView)
                //add action buttons
            .setPositiveButton(R.string.generate, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TextView accountNumberTV = (TextView) dialogView.findViewById(R.id.account_number);
                    TextView bankCodeTV = (TextView) dialogView.findViewById(R.id.bank_code);
                    TextView amountTV = (TextView) dialogView.findViewById(R.id.amount);
                    //todo do validation
                    int amount = (amountTV.getText().toString().isEmpty()) ? 0 :
                            Integer.parseInt(amountTV.getText().toString());
                    String accountNumber = accountNumberTV.getText().toString();
                    String bankCode = bankCodeTV.getText().toString();
                    generateQrCode(bankCode, accountNumber, amount);
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //cancel dialog
                }
            })
        .show();
    }

    private void generateQrCode(String bankCode, String accountNumber, int amount) {
        AddQrCodeViewModel addViewModel = ViewModelProviders
                .of(this).get(AddQrCodeViewModel.class);

        String uri = "ferapid://transfer?"+
                getResources().getString(R.string.ferapid_uri_param_bank_code)+
                "="+bankCode+
                "&"+getResources().getString(R.string.ferapid_uri_param_account_number)+
                "="+accountNumber+
                "&"+getResources().getString(R.string.ferapid_uri_param_amount)+
                "="+amount;
        Bitmap qrBitmap = QRCode.from(uri).bitmap();
        String qrImagePath = Utils.saveBitmapToStorage(this, qrBitmap);
        ImageView qrView = findViewById(R.id.qrcodeView);
        qrView.setImageBitmap(qrBitmap);
        qrView.setVisibility(View.VISIBLE);
        xyz.mchl.ferapid.persistence.QRCode qrCode = new xyz.mchl.ferapid.persistence.QRCode(
                amount,
                accountNumber,
                bankCode,
                Utils.fetchBankList().get(bankCode)
        );
        qrCode.setQrImagePath(qrImagePath);
        addViewModel.addQrCode(qrCode);
        Log.d("QRPath", qrImagePath);
    }
}
