package xyz.ruslang.task16;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button tokenBtn = findViewById(R.id.log_token);
        Message message = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setToken(registrationToken)
                .build();

// Send a message to the devices subscribed to the provided topic.
        String response = FirebaseMessaging.getInstance().send(message);
// Response is a message ID string.
        System.out.println("Successfully sent message: " + response);
        RemoteMessage message = new RemoteMessage.Builder().
        FirebaseMessaging.getInstance().send();
        tokenBtn.setOnClickListener(v -> {
            String tkn = FirebaseInstanceId.getInstance().getToken();
            Toast.makeText(MainActivity.this, "Current token ["+tkn+"]",
                    Toast.LENGTH_LONG).show();
            Log.d("App", "Token ["+tkn+"]");
        });
    }
}