package de.tsbw.call;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class ManualDialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_dial);

        EditText phoneNumberInput = findViewById(R.id.phoneNumberInput);
        if (phoneNumberInput.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void makeCall(View view) {
        EditText phoneNumberInput = findViewById(R.id.phoneNumberInput);
        String number = phoneNumberInput.getText().toString();
        NumberRewriteEngine.placeCall(this, number);
        phoneNumberInput.setText("");

    }
}
