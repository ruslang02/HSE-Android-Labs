package com.devexito.nexomia.ui.messages;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devexito.nexomia.R;
import com.devexito.nexomia.api.NVWSClient;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.ViewHolder> {
    private final FragmentActivity activity;
    private final JSONArray dataSet;

    public MessagesListAdapter(FragmentActivity activity, JSONArray dataSet) {
        this.dataSet = dataSet;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            JSONObject obj = dataSet.getJSONObject(position);
            holder.message_content.setText(obj.getString("text"));
            holder.message_date.setText(new Date(obj.getInt("created")).toString());
            JSONObject user = NVWSClient.getInstance().getUser(obj.getString("user"));
            holder.message_author.setText(user.getString("name"));
            if (user.getString("avatar").equals("")) {
                Picasso.get().load("http://nexo.fun:8084/img/defaultAvatar.3304bd4b.png").into(holder.message_avatar);
            } else {
                Picasso.get().load(user.getString("avatar")).into(holder.message_avatar);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final CircularImageView message_avatar;
        public final TextView message_author;
        public final TextView message_date;
        public final TextView message_content;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            message_author = (TextView) view.findViewById(R.id.message_author);
            message_date = (TextView) view.findViewById(R.id.message_date);
            message_content = (TextView) view.findViewById(R.id.message_content);
            message_avatar = (CircularImageView) view.findViewById(R.id.message_avatar);
        }
    }
}