package com.devexito.nexomia.ui.channels_list;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.devexito.nexomia.R;
import com.devexito.nexomia.api.NVWSClient;
import com.devexito.nexomia.ui.EmailVerifyActivity;
import com.devexito.nexomia.ui.LoginActivity;
import com.devexito.nexomia.ui.MainActivity;
import com.devexito.nexomia.ui.messages.MessagesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChannelsListAdapter extends RecyclerView.Adapter<ChannelsListAdapter.ViewHolder> {
    private final FragmentActivity activity;
    private final JSONArray dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button chButton;

        public ViewHolder(View view) {
            super(view);
            chButton = (Button) view.findViewById(R.id.channel_button);
        }

        public Button getButton() {
            return chButton;
        }
    }

    public ChannelsListAdapter(FragmentActivity activity, JSONArray dataSet) {
        this.dataSet = dataSet;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.channel_button, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        try {
            final JSONObject channel = dataSet.getJSONObject(position);
            viewHolder.getButton().setText(channel.getString("title"));
            viewHolder.getButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        NVWSClient.getInstance().setChannel(channel.getString("id"), channel.getString("title"));

                        Intent intent = new Intent(activity, MessagesActivity.class);
                        activity.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.length();
    }
}
