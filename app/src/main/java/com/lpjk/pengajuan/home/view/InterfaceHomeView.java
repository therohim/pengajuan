package com.lpjk.pengajuan.home.view;

import android.app.Activity;
import android.content.Context;

import org.json.JSONObject;

public interface InterfaceHomeView {
    void onTotal(JSONObject jsonObject);
    void onProfil(String nama, String nik);
    Activity getContext();
    void End();
}
