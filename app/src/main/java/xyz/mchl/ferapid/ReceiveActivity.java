package xyz.mchl.ferapid;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class ReceiveActivity extends AppCompatActivity {

    Button buttonNewCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.dialog_generate_code, null))
                //add action buttons
            .setPositiveButton(R.string.generate, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //generate code
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
}
