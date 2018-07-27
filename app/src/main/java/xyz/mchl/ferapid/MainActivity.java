package xyz.mchl.ferapid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button sendButton;
    Button receiveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = (Button) findViewById(R.id.button_send);
        receiveButton = (Button) findViewById(R.id.button_receive);

        activateListeners();
    }

    private void activateListeners() {
        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent receiveIntent =  new Intent(MainActivity.this, ReceiveActivity.class);
                startActivity(receiveIntent);
            }
        });
    }
}
