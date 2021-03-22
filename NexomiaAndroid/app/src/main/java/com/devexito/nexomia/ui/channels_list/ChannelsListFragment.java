package com.devexito.nexomia.ui.channels_list;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.devexito.nexomia.R;
import com.devexito.nexomia.api.NVWSClient;
import com.devexito.nexomia.api.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChannelsListFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View view = inflater.inflate(R.layout.fragment_channels_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            Map<String, String> params = new HashMap<>();
            params.put("server", NVWSClient.getInstance().getServerId());
            ((RecyclerView) view).setLayoutManager(new LinearLayoutManager(context));
            Log.i("CLF", NVWSClient.getInstance().getServerId());
            NVWSClient.getInstance().sendRequest(Request.Method.POST, "http://nexo.fun:8081/api/channels/list", new RequestListener() {
                @Override
                public void onFinish(JSONObject object) {
                    try {
                        if (NVWSClient.getInstance().getServerId().equals("@me")) {
                            JSONArray channels = new JSONArray("[{\"id\": \"5607331430493450730\", \"title\": \"ruslang02\"}]");
                            ((RecyclerView) view).setAdapter(new ChannelsListAdapter(getActivity(), channels));
                        } else {
                            JSONArray channels = object.getJSONArray("channels");
                            ((RecyclerView) view).setAdapter(new ChannelsListAdapter(getActivity(), channels));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, params);
            NVWSClient.getInstance().fetchUsers();
        }

        return view;
    }
}