package xyz.ruslang.task15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText track_name;
    private EditText track_artist;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        track_name = findViewById(R.id.track_name);
        track_artist = findViewById(R.id.track_artist);
        SharedPreferences save = getSharedPreferences("SAVE",0);
        track_name.setText(save.getString("track_name",""));
        track_artist.setText(save.getString("track_artist",""));
        Button addBtn = findViewById(R.id.add_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteStatement stmt = db.compileStatement("INSERT INTO songs VALUES(null, ?, ?)");
                stmt.bindString(1, track_name.getText().toString());
                stmt.bindString(2, track_artist.getText().toString());
                stmt.executeInsert();
                updateFromDb();
            }
        });
        Button deleteBtn = findViewById(R.id.delete_all);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete("songs", null, null);
                updateFromDb();
            }
        });
    }

    private void updateFromDb() {
        TextView text = findViewById(R.id.textView);
        StringBuilder builder = new StringBuilder("База данных:\n");
        Cursor cursor = db.rawQuery("SELECT * FROM songs", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String tr_name = cursor.getString(cursor.getColumnIndex("title"));
                String tr_artist = cursor.getString(cursor.getColumnIndex("artist"));

                builder.append(tr_name).append(", исполняет: ").append(tr_artist).append("\n");
                cursor.moveToNext();
            }
        }
        text.setText(builder.toString());
        cursor.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences save = getSharedPreferences("SAVE",0);
        SharedPreferences.Editor editor = save.edit();
        editor.putString("track_name",track_name.getText().toString());
        editor.putString("track_artist",track_artist.getText().toString());
        editor.apply();
        db.close();
    }


    @Override
    protected void onStart() {
        super.onStart();
        db = openOrCreateDatabase("RMusic", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS songs (id integer PRIMARY KEY, title text, artist text)");
        updateFromDb();
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
            case R.id.about:
                AlertDialog.Builder dialog = new
                        AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("About this app");
                dialog.setMessage(R.string.author);
                dialog.setCancelable(true);
                dialog.setNeutralButton("Okay", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                break;
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this,
                        SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }


}