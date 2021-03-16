package com.devexito.nexomia.api;

import android.content.Context;
import android.util.Base64;
import android.util.Log;


import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.ClientError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NVWSClient {
    private WebSocket ws;

    private RequestQueue queue;

    private String email;

    private String token;

    private String serverId = "@me";

    private String channelId = "";

    private String channelName = "";

    private JSONArray users;

    static NVWSClient instance = null;

    public void setContext(Context ctx) {
        queue = Volley.newRequestQueue(ctx);
        Log.i(this.getClass().getSimpleName(), "Context set.");
    }

    public static NVWSClient getInstance() {
        if (instance == null) {
            Log.i("NVWS", "Instance created.");
            instance = new NVWSClient();
        }

        return instance;
    }

    public String getToken() {
        return token;
    }

    private NVWSClient() {
        this("eyJpZCI6IjU2MDczMzE0MzA0OTM0NTA3MzAiLCJmcCI6ImY5Njk1NTI3NDY3MDc1M2JiMDFjMzgyYzZhODc5NDM1IiwiaWF0IjoxNjE1MTMwODEyfQ.fPqiczd-FYNK2fcI3iAjhCjJI_qTTylaZJgCWf4-zGw");
    }

    private NVWSClient(String token) {
        try {
            ws = new WebSocketFactory().createSocket("ws://nexo.fun:8085/" + token);
            ws.setDirectTextMessage(false);
            ws.addExtension("permessage-deflate");
            ws.addExtension("client_max_window_bits");
            ws.addListener(new WebSocketAdapter() {
                @Override
                public void onError(WebSocket websocket,
                                    WebSocketException cause) {
                    Log.i(this.getClass().getName(), cause.getMessage());
                }

                @Override
                public void onConnected(WebSocket websocket,
                                        Map<String, List<String>> headers) {
                    Log.i(this.getClass().getName(), headers.size() + "");
                }

                @Override
                public void onFrame(WebSocket websocket,
                                    WebSocketFrame frame) {

                    Log.i(this.getClass().getName(), frame.getPayloadText() + " frame");
                }

                @Override
                public void onStateChanged(WebSocket websocket, WebSocketState newState) {

                    Log.i(this.getClass().getName(), "state " + newState.name());
                }

                @Override
                public void onDisconnected(WebSocket websocket,
                                           WebSocketFrame serverCloseFrame,
                                           WebSocketFrame clientCloseFrame,
                                           boolean closedByServer) {
                    Log.i(this.getClass().getName(), closedByServer + serverCloseFrame.getCloseReason());
                }

                @Override
                public void onTextMessage(WebSocket websocket,
                                          String text) {
                    Log.i(this.getClass().getName(), new String(Base64.decode(text.getBytes(), 0)));
                }

                @Override
                public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) {
                    Log.i(this.getClass().getName(), cause.getMessage() + frame.getPayloadText());
                }

                @Override
                public void onUnexpectedError(WebSocket websocket, WebSocketException cause) {
                    Log.i(this.getClass().getName(), cause.getMessage());
                }
            });
        } catch (IOException e) {
            Log.i(this.getClass().getName(), e.getMessage());
        }
    }

    public void sendRequest(
            int method,
            String url,
            final RequestListener listener) {
        sendRequest(method, url, listener, new HashMap<String, String>());
    }

    public void sendRequest(
            int method,
            String url,
            final RequestListener listener,
            final Map<String, String> params) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("auth", token);
        headers.put("User-Agent", "android.com.devexito.nexomia");

        sendRequest(method, url, listener, params, headers);
    }

    public void sendRequest(
            int method,
            String url,
            final RequestListener listener,
            final Map<String, String> params,
            final Map<String, String> headers
    ) {
        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(this.getClass().getName(), response);
                        try {
                            JSONObject resp = new JSONObject(response);
                            listener.onFinish(resp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                try {
                    JSONObject resp = new JSONObject(new String(error.networkResponse.data));
                    listener.onFinish(resp);
                } catch (JSONException e) {
                    Log.e(this.getClass().getSimpleName(), new String(error.networkResponse.data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        queue.add(stringRequest);
    }

    public void login(final String email, final String password, final LoginListener listener) {
        this.email = email;

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("User-Agent", "android.com.devexito.nexomia");

        sendRequest(
                Request.Method.POST,
                "http://nexo.fun:8081/api/auth/login",
                new RequestListener() {
                    @Override
                    public void onFinish(JSONObject resp) {
                        try {
                            switch (resp.getString("msg")) {
                                case "logged_in":
                                case "verified":
                                    token = resp.getString("token");
                                    listener.onSuccess(token);
                                    break;
                                case "email_verify":
                                    listener.onEmailVerify();
                                    break;
                                case "password_invalid":
                                case "email_invalid":
                                    listener.onError("The email or password was invalid.");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError(e.getMessage());
                        }
                    }
                },
                params,
                headers);
    }

    public void verify(final String code, final LoginListener listener) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("code", code);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("User-Agent", "android.com.devexito.nexomia");

        sendRequest(Request.Method.POST, "http://nexo.fun:8081/api/auth/verifyEmail",
                new RequestListener() {
                    @Override
                    public void onFinish(JSONObject resp) {
                        try {
                            switch (resp.getString("msg")) {
                                case "code_invalid":
                                    listener.onError("The code you entered was invalid.");
                                    break;
                                case "verified":
                                    token = resp.getString("token");
                                    listener.onSuccess(token);
                                    break;
                            }
                        } catch (JSONException e) {
                            listener.onError(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                params,
                headers);
    }

    public void setStatus() {
        try {
            JSONObject obj = new JSONObject("{\"type\":\"presence_update\",\"data\":{\"query\":{\"id\":3}}}");
            send(obj);
        } catch (JSONException e) {
            Log.i(this.getClass().getName(), e.getMessage());
        }
    }

    public void connect() {
        ws.connectAsynchronously();
    }

    public void send(JSONObject data) {
        byte[] encoded = Base64.encode(data.toString().getBytes(), Base64.DEFAULT);
        Log.i(this.getClass().getName(), data.toString());

        ws.sendBinary(encoded);
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannel(String channelId, String channelName) {
        this.channelId = channelId;
        this.channelName = channelName;
    }

    public void fetchUsers() {
        Map<String, String> params = new HashMap<>();
        params.put("server", serverId);

        sendRequest(Request.Method.POST, "http://nexo.fun:8081/api/servers/listMembers", new RequestListener() {
            @Override
            public void onFinish(JSONObject object) {
                try {
                    users = object.getJSONArray("members");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);
    }

    public JSONObject getUser(String userId) {
        try {
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString("id").equals(userId))
                    return user;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
