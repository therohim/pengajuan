package com.lpjk.pengajuan.akun.view;

import android.app.Activity;
import android.content.Context;

import org.json.JSONObject;

public interface InterfaceAkunView {
    void onUpdateProfilSuccess(String message);
    void onUpdateProfilError(String message);
    void onUpdatePassSuccess(String message);
    void onUpdatePassError(String message);
    void End();
    Activity getContext();
    void getDataProfil(JSONObject jsonObject);
}
