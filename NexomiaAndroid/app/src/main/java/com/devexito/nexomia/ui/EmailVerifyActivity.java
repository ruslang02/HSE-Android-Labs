package com.devexito.nexomia.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.devexito.nexomia.R;
import com.devexito.nexomia.api.LoginListener;
import com.devexito.nexomia.api.NVWSClient;

public class EmailVerifyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);
        Button verifyBtn = (Button) findViewById(R.id.verifyBtn);
        final EditText verifyCodeField = (EditText) findViewById(R.id.verifyCodeField);
        final TextView verifyMessage = (TextView) findViewById(R.id.verifyMessage);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NVWSClient.getInstance().verify(verifyCodeField.getText().toString(), new LoginListener() {
                    @Override
                    public void onSuccess(String token) {
                        finish();
                        finishActivity(10);
                    }

                    @Override
                    public void onEmailVerify() {

                    }

                    @Override
                    public void onError(String message) {
                        verifyMessage.setText(message);
                        verifyMessage.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}