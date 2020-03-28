package com.rohim.rohimmodule;

import org.json.JSONException;
import org.json.JSONObject;

public interface FetchDataListener {
    void onFetchComplete(JSONObject data) throws JSONException;

    void onFetchFailure(String msg);

    void onFetchStart();
}
