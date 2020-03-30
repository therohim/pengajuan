package com.lpjk.pengajuan.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lpjk.pengajuan.MainActivity;
import com.lpjk.pengajuan.R;
import com.lpjk.pengajuan.login.presenter.InterfaceLoginPresenter;
import com.lpjk.pengajuan.login.presenter.LoginPresenter;
import com.lpjk.pengajuan.login.view.IloginView;
import com.rohim.rohimmodule.RequestQueueService;
import com.rohim.rohimmodule.UserSession;

public class LoginActivity extends AppCompatActivity implements IloginView {
    EditText edtUsername, edtPassword;
    AppCompatButton btnLogin;
    private boolean doubleTapParam = false;
    InterfaceLoginPresenter loginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();
    }

    private void initUi(){
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        loginPresenter = new LoginPresenter(this);
        UserSession session =new UserSession(getApplicationContext());
        if (session.getSPSudahLogin()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String pass = edtPassword.getText().toString();
                loginPresenter.onLogin(username,pass);
            }
        });
    }

    @Override
    public void onLoginSuccess(String message) {
    }

    @Override
    public void onLoginError(String message) {
        RequestQueueService.showAlertError(message,this);
    }

    @Override
    public void End() {
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showProgress() {
        RequestQueueService.showProgressDialog(this);
    }

    @Override
    public void dismisProgress() {
        RequestQueueService.cancelProgressDialog();
    }

    @Override
    public void onBackPressed() {
        if (doubleTapParam) {
            super.onBackPressed();
            return;
        }

        this.doubleTapParam = true;
        Toast.makeText(this, "Tap sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleTapParam = false;
            }
        }, 2000);
    }
}
