package com.devexito.nexomia.ui.messages;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.devexito.nexomia.R;
import com.devexito.nexomia.api.NVWSClient;

public class MessagesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_messages);
        Toolbar bar = (Toolbar) findViewById(R.id.messages_toolbar);
        bar.setTitle("#" + NVWSClient.getInstance().getChannelName());
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
