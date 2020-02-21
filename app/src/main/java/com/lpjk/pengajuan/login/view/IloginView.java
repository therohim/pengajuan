package org.pindaiaja.pindaiapp.login.view;

import android.content.Context;

public interface IloginView {
    void onLoginSuccess(String message);
    void onLoginError(String message);
    void End();
    Context getContext();
    void onSetProgressBarVisibility(int visibility);
    void setFooterLogin(String copyright);
}
