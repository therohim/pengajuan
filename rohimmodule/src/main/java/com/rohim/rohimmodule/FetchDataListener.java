package com.rohim.rohimmodule;

import org.json.JSONObject;

public interface FetchDataListener {
    void onFetchComplete(JSONObject data);

    void onFetchFailure(String msg);

    void onFetchStart();
}
