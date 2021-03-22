package com.devexito.nexomia.api;

import org.json.JSONObject;

public interface MessageListener {
    void onMessageCreate(JSONObject message);
}
