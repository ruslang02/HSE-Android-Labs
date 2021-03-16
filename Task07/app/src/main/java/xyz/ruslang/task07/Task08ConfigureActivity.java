package xyz.ruslang.task07;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

/**
 * The configuration screen for the {@link Task08 Task08} AppWidget.
 */
public class Task08ConfigureActivity extends Activity {
    private Task08ConfigureActivity context;
    private int widgetID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task08_configure);
        setResult(RESULT_CANCELED);
        context = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            widgetID =
                    extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID);
            final AppWidgetManager widgetManager =
                    AppWidgetManager.getInstance(context);
            final RemoteViews views = new
                    RemoteViews(context.getPackageName(), R.layout.task08);
            final EditText editText = (EditText)
                    findViewById(R.id.appwidget_text);
            Button button = (Button) findViewById(R.id.add_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_VIEW,
                            Uri.parse(editText.getText().toString()));
                    PendingIntent pending= PendingIntent.getActivity(context, 0,
                            intent, 0);
                    views.setOnClickPendingIntent(R.id.appwidget_text, pending);
                    widgetManager.updateAppWidget(widgetID, views);
                    Intent resultValue = new Intent();
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                            widgetID);
                    setResult(RESULT_OK, resultValue);
                    finish();
                }
            });
        }
    }
}

