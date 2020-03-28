package com.lpjk.pengajuan.login.view;

import android.content.Context;

public interface IloginView {
    void onLoginSuccess(String message);
    void onLoginError(String message);
    void End();
    Context getContext();
    void showProgress();
    void dismisProgress();
}
