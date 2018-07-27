package xyz.mchl.ferapid;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.glxn.qrgen.android.QRCode;

import org.w3c.dom.Text;

public class ReceiveActivity extends AppCompatActivity {

    Button buttonNewCode;

    public static String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        TAG = getClass().getName();

        buttonNewCode = (Button) findViewById(R.id.new_code);

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
        String uri = "ferapid://transfer?bankCode="+bankCode+"&account="+accountNumber+"&amount="+amount;
        Bitmap qrBitmap = QRCode.from(uri).bitmap();
        ImageView qrView = findViewById(R.id.qrcodeView);
        qrView.setImageBitmap(qrBitmap);
        qrView.setVisibility(View.VISIBLE);
    }
}
