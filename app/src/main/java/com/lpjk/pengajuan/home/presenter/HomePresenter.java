package com.lpjk.pengajuan.home.presenter;

import android.util.Log;

import com.lpjk.pengajuan.home.view.InterfaceHomeView;
import com.lpjk.pengajuan.utils.ServerUrl;
import com.rohim.rohimmodule.FetchDataListener;
import com.rohim.rohimmodule.RequestQueueService;
import com.rohim.rohimmodule.UserSession;
import com.rohim.rohimmodule.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomePresenter implements InterfaceHomePresenter {
    InterfaceHomeView homeView;
    UserSession session;

    public HomePresenter(InterfaceHomeView view){
        this.homeView = view;
    }

    @Override
    public void getTotalDashboard() {
        try{
            VolleyRequest postapiRequest=new VolleyRequest();
            JSONObject jsonObject = new JSONObject();
            postapiRequest.request(homeView.getContext(),fetchResultTotal,jsonObject, ServerUrl.url_get_total,"GET");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    FetchDataListener fetchResultTotal = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) throws JSONException {
            RequestQueueService.cancelProgressDialog();
            JSONObject metadata= new JSONObject(data.optString("metadata"));
            int status = metadata.getInt("status");
            String message =metadata.getString("message");
            if(status == 200) {
                JSONObject res  = data.getJSONObject("response");
                homeView.onTotal(res);
            }
        }

        @Override
        public void onFetchFailure(String msg) {

        }

        @Override
        public void onFetchStart() {

        }
    };

    @Override
    public void getProfil() {
        session = new UserSession(homeView.getContext());
        try{
            VolleyRequest postapiRequest=new VolleyRequest();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", session.getSpUserId());
            }catch (Exception e){
                e.printStackTrace();
            }
            postapiRequest.request(homeView.getContext(),getDataProfil,jsonObject, ServerUrl.url_get_profile,"POST");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    FetchDataListener getDataProfil  = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) throws JSONException {
            RequestQueueService.cancelProgressDialog();
            JSONObject metadata= new JSONObject(data.optString("metadata"));
            int status = metadata.getInt("status");
            String message =metadata.getString("message");
            if(status == 200){
                JSONObject response = new JSONObject(data.optString("response"));
                Log.d(">>>>>>",String.valueOf(response));
                String nama = response.getString("nama");
                String nik = response.getString("nik");
                homeView.onProfil(nama,nik);
            }else{
                RequestQueueService.showAlertError(message,homeView.getContext());
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlertError(msg,homeView.getContext());
        }

        @Override
        public void onFetchStart() {
            RequestQueueService.showProgressDialog(homeView.getContext());
        }
    };
}
