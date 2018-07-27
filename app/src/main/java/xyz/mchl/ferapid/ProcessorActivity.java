package xyz.mchl.ferapid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public class ProcessorActivity extends AppCompatActivity {

    public static String EXTRA_URI_DATA = "barcode_uri";
    String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processor);

        barcode  = getIntent().getStringExtra(EXTRA_URI_DATA);

        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(this, "No data to process", Toast.LENGTH_SHORT).show();
            finish();
        }

        //process data
        Toast.makeText(this, barcode,Toast.LENGTH_SHORT).show();
    }
}
