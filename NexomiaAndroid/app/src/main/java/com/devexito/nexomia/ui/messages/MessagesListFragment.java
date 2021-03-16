package com.devexito.nexomia.ui.messages;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.devexito.nexomia.R;
import com.devexito.nexomia.api.NVWSClient;
import com.devexito.nexomia.api.RequestListener;
import com.devexito.nexomia.ui.channels_list.ChannelsListAdapter;
import com.devexito.nexomia.ui.dummy.DummyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 */
public class MessagesListFragment extends Fragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MessagesListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MessagesListFragment newInstance(int columnCount) {
        MessagesListFragment fragment = new MessagesListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_messages_2, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            Map<String, String> params = new HashMap<>();
            params.put("offset", "0");
            params.put("count", "0");
            params.put("channel", NVWSClient.getInstance().getChannelId());
            ((RecyclerView) view).setLayoutManager(new LinearLayoutManager(context));
            Log.i("CLF", NVWSClient.getInstance().getServerId());

            NVWSClient.getInstance().sendRequest(Request.Method.POST, "http://nexo.fun:8081/api/channels/getChannelMessages", new RequestListener() {
                @Override
                public void onFinish(JSONObject object) {
                    try {
                        JSONArray messages = object.getJSONArray("messages");
                        ((RecyclerView) view).setAdapter(new MessagesListAdapter(getActivity(), messages));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, params);
        }

        return view;
    }
}