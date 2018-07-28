package xyz.mchl.ferapid;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
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
import android.widget.Toast;

import net.glxn.qrgen.android.QRCode;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xyz.mchl.ferapid.junk.Utils;

public class ReceiveActivity extends AppCompatActivity {

    Button buttonNewCode;
    Button shareButton;

    public static String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        TAG = getClass().getName();
        buttonNewCode = findViewById(R.id.new_code);

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
        shareButton = findViewById(R.id.button_share);
        qrView.setImageBitmap(qrBitmap);
        qrView.setVisibility(View.VISIBLE);
        xyz.mchl.ferapid.persistence.QRCode qrCode = new xyz.mchl.ferapid.persistence.QRCode(
                amount,
                accountNumber,
                bankCode,
                Utils.fetchBankList().get(bankCode)
        );

        shareButton.setOnClickListener(handleShareButtonClick(qrBitmap,qrImagePath));
        shareButton.setVisibility(View.VISIBLE);

        qrCode.setQrImagePath(qrImagePath);
        addViewModel.addQrCode(qrCode);
        Log.d("QRPath", qrImagePath);
    }

    private View.OnClickListener handleShareButtonClick(final Bitmap bitmap, final String imageFileName) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    String imageUrl = MediaStore.Images.Media.insertImage(getContentResolver(),
                            bitmap, imageFileName, "description");
                    Uri savedImageUri = Uri.parse(imageUrl);
                    if (savedImageUri != null) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("image/png");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, savedImageUri);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareIntent, "Send To..."));
                    }
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissions has been granted. Share your code with pride!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ferapid requires internet and storage access. Please check your" +
                            " permission settings.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
