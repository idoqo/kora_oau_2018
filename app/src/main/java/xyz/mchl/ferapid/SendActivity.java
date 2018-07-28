package xyz.mchl.ferapid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;
import okhttp3.internal.Util;
import xyz.mchl.ferapid.junk.Utils;

public class SendActivity extends AppCompatActivity
    implements BarcodeReader.BarcodeReaderListener{

    public static String BASE_URL = "https://moneywave.herokuapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action)) {
            if (("image/*").equals(type)) {
                processCodeFromImage(intent);
            }
        }
    }

    private void processCodeFromImage(Intent intent) {
        Uri imageUri = (Uri)intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            try {
                Bitmap bitmap = Utils.decodeUri(this, imageUri, 200);
                //setup barcode detector
                BarcodeDetector detector =
                        new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.DATA_MATRIX|Barcode.QR_CODE)
                        .build();
                if (!detector.isOperational()) {
                    Toast.makeText(SendActivity.this, "Could not setup detector",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                Frame visionFrame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<Barcode> barcodes = detector.detect(visionFrame);
                Barcode target = barcodes.valueAt(0);
                this.onScanned(target);
            } catch (Exception ioe) {
                Toast.makeText(SendActivity.this, ioe.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onScanned(Barcode barcode) {
        Log.d("SendActivity", barcode.displayValue);
        Intent processorIntent = new Intent(SendActivity.this, ProcessorActivity.class);
        processorIntent.putExtra(ProcessorActivity.EXTRA_URI_DATA, barcode.displayValue);
        startActivity(processorIntent);
    }

    public void onScannedMultiple(List<Barcode> barcodeList) {

    }

    @Override
    public void onCameraPermissionDenied() {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }
}
