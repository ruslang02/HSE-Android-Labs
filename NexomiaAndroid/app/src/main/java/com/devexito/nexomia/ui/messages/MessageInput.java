package com.devexito.nexomia.ui.messages;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.devexito.nexomia.R;
import com.devexito.nexomia.api.NVWSClient;

public class MessageInput extends Fragment {
    public MessageInput() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_input, container, false);
        final EditText sendField = (EditText) view.findViewById(R.id.message_input_field);
        Button attachBtn = (Button) view.findViewById(R.id.message_input_attachment);
        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, 100);
            }
        });
        Button sendBtn = (Button) view.findViewById(R.id.message_input_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NVWSClient.getInstance().sendMessage(sendField.getText().toString());
                sendField.setText("");
            }
        });
        return view;
    }
}