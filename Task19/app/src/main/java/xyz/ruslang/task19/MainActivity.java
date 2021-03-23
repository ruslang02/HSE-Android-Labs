package xyz.ruslang.task19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private String generateSeed() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 24; i++) {
            builder.append((int) Math.floor(Math.random() * 11));
        }
        return builder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CircularImageView imageView = findViewById(R.id.imageView);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String seed = generateSeed();
                Picasso.with(MainActivity.this)
                        .load("https://www.gravatar.com/avatar/" + seed + "?s=1024&d=identicon&r=PG")
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(imageView);
            }
        });
    }
}