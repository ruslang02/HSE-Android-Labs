package xyz.ruslang.task06;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation =
                        AnimationUtils.loadAnimation(MainActivity.this,
                                R.anim.btn_animation);
                v.startAnimation(animation);
            }
        });
        findViewById(R.id.editTextTextPersonName).setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Animation animation =
                        AnimationUtils.loadAnimation(MainActivity.this,
                                R.anim.line_animation);
                v.startAnimation(animation);
                return false;
            }
        });
    }
}