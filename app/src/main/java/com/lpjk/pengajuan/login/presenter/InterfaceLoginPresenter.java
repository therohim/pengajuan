package org.pindaiaja.pindaiapp.login.presenter;

public interface InterfaceLoginPresenter {
    void onLogin(String email, String password) ;
    void setProgressBarVisiblity(int visiblity);
    void footerLogin();
}