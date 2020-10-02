package xyz.ruslang.task02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActivity();
    }

    protected void initActivity() {
        final TextView textView = (TextView) findViewById(R.id.textView2);
        final EditText editText = (EditText) findViewById(R.id.editText);
        Button setButton = (Button) findViewById(R.id.button);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Your favourite pet is " + editText.getText().toString() + "!");
            }
        });
    }
}