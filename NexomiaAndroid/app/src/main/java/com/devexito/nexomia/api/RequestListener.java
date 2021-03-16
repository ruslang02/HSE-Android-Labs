package com.devexito.nexomia.api;

import org.json.JSONObject;

public interface RequestListener {
    void onFinish(JSONObject object);
}
