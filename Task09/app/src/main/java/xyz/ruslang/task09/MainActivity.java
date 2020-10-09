package xyz.ruslang.task09;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        registerForContextMenu(textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.toggle_text:
            if (!item.isChecked()){
                textView.setVisibility(TextView.VISIBLE);
                item.setChecked(true);
            }
            else {
                textView.setVisibility(TextView.INVISIBLE);
                item.setChecked(false);
            }
            break;
            case R.id.about_button:
                Toast.makeText(MainActivity.this, R.string.author,  Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.red_button){
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setTextColor(Color.parseColor("red"));
        }
        if (id == R.id.black_button){
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setTextColor(Color.parseColor("black"));
        }
        return super.onContextItemSelected(item);
    }
}