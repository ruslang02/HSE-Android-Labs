package com.devexito.nexomia.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.devexito.nexomia.R;
import com.devexito.nexomia.api.LoginListener;
import com.devexito.nexomia.api.NVWSClient;

public class LoginActivity extends Activity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (NVWSClient.getInstance().getToken() == null) {
            System.exit(0);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (NVWSClient.getInstance().getToken() != null) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NVWSClient.getInstance().setContext(getApplicationContext());
        setContentView(R.layout.activity_login);
        Button signInBtn = (Button) findViewById(R.id.signInBtn);
        final EditText emailField = (EditText) findViewById(R.id.emailField);
        final EditText passwordField = (EditText) findViewById(R.id.passwordField);
        final TextView messageBox = (TextView) findViewById(R.id.messageBox);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NVWSClient.getInstance().login(emailField.getText().toString(), passwordField.getText().toString(), new LoginListener() {
                    @Override
                    public void onSuccess(String token) {
                        finish();
                    }

                    @Override
                    public void onEmailVerify() {
                        Intent intent = new Intent(LoginActivity.this,
                                EmailVerifyActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String message) {
                        Log.i("sss", message);
                        messageBox.setText(message);
                        messageBox.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}