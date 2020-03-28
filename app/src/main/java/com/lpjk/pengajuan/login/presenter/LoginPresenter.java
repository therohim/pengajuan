package com.lpjk.pengajuan.login.presenter;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.lpjk.pengajuan.MainActivity;
import com.lpjk.pengajuan.login.model.Login;
import com.lpjk.pengajuan.login.view.IloginView;
import com.lpjk.pengajuan.utils.ServerUrl;
import com.rohim.rohimmodule.FetchDataListener;
import com.rohim.rohimmodule.RequestQueueService;
import com.rohim.rohimmodule.UserSession;
import com.rohim.rohimmodule.VolleyRequest;
import com.rohim.rohimmodule.toast.ToastBeautify;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginPresenter implements InterfaceLoginPresenter {
    IloginView loginView;
    UserSession session;

    public LoginPresenter(IloginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void onLogin(String username, String password) {
        try{
            VolleyRequest postapiRequest=new VolleyRequest();
            session = new UserSession(loginView.getContext());
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", username);
                jsonObject.put("password", password);
            }catch (Exception e){
                e.printStackTrace();
            }
            postapiRequest.request(loginView.getContext(),fetchPostResultListener,jsonObject,ServerUrl.url_login,"POST");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    FetchDataListener fetchPostResultListener=new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) throws JSONException {
            RequestQueueService.cancelProgressDialog();
            JSONObject metadata= new JSONObject(data.optString("metadata"));
            int status = metadata.getInt("status");
            String message =metadata.getString("message");

            if(status == 200){
                JSONObject response = new JSONObject(data.optString("response"));
                String user_id= response.getString("id");
                String nama = response.getString("nama");
                String foto_profil = response.getString("foto_profil");
                session.saveSPString(UserSession.SP_USER_ID, user_id);
                session.saveSPString(UserSession.SP_NAMA,nama);
                session.saveSPString(UserSession.SP_FP,foto_profil);
                session.saveSPBoolean(UserSession.SP_SUDAH_LOGIN, true);
                loginView.getContext().startActivity(new Intent(loginView.getContext(), MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                loginView.End();
            }else{
                loginView.onLoginError(message);
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlertError(msg,loginView.getContext());
        }

        @Override
        public void onFetchStart() {
            RequestQueueService.showProgressDialog(loginView.getContext());
        }
    };
}
