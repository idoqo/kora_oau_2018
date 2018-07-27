package xyz.mchl.ferapid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class SendActivity extends AppCompatActivity
    implements BarcodeReader.BarcodeReaderListener{

    public static String BASE_URL = "https://moneywave.herokuapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
    }

    public void onScanned(Barcode barcode) {
        Log.d("SendActivity", barcode.displayValue);
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
