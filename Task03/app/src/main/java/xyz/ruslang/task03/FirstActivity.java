package xyz.ruslang.task03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FirstActivity extends AppCompatActivity {
    public static String enteredName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText nameEdit = (EditText) findViewById(R.id.editTextTextPersonName);
        final Button nextBtn = (Button) findViewById(R.id.button);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent switchIntent = new Intent(FirstActivity.this, SecondActivity.class);
                enteredName = nameEdit.getText().toString();
                if (enteredName.length() < 1) {
                    nextBtn.setTextColor(Color.parseColor("#FF0000"));
                } else startActivity(switchIntent);
            }
        });
    }
}