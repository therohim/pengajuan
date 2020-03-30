package com.lpjk.pengajuan.permohonan.sbu.presenter;

import com.lpjk.pengajuan.login.model.Login;
import com.lpjk.pengajuan.permohonan.sbu.model.SbuModel;
import com.lpjk.pengajuan.permohonan.sbu.view.SbuView;
import com.lpjk.pengajuan.utils.ServerUrl;
import com.rohim.rohimmodule.FetchDataListener;
import com.rohim.rohimmodule.RequestQueueService;
import com.rohim.rohimmodule.UserSession;
import com.rohim.rohimmodule.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SbuPresenter implements ISbuPresenter {
    SbuView sbuView;
    UserSession session;
    List<SbuModel> model;

    public SbuPresenter(SbuView sbuView){
        this.sbuView = sbuView;
    }

    @Override
    public void getSbu() {
        session = new UserSession(sbuView.getContext());
        try{
            VolleyRequest postapiRequest=new VolleyRequest();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id_user", session.getSpUserId());
            }catch (Exception e){
                e.printStackTrace();
            }
            postapiRequest.request(sbuView.getContext(),fetchResultSbu,jsonObject, ServerUrl.url_get_sbu,"POST");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    FetchDataListener fetchResultSbu = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) throws JSONException {
            RequestQueueService.cancelProgressDialog();
            JSONObject metadata= new JSONObject(data.optString("metadata"));
            int status = metadata.getInt("status");
            String message =metadata.getString("message");
            if(status == 200){
                model = new ArrayList<>();
                JSONArray object  = data.getJSONArray("response");
                for (int i =0; i<object.length(); i++){
                    JSONObject obj = object.getJSONObject(i);
                    model.add(new SbuModel(
                            obj.getInt("id"),
                            obj.getString("npwp"),
                            obj.getString("nama"),
                            obj.getString("alamat"),
                            obj.getString("kota"),
                            obj.getString("kbli"),
                            obj.getString("token_permohonan"),
                            obj.getString("token_klasifikasi"),
                            obj.getInt("status_approval")
                    ));
                    sbuView.onGetResultSbu(model);
                }
            }
//            else{
//                RequestQueueService.showAlertError(message,sbuView.getContext());
//            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlertError(msg,sbuView.getContext());
        }

        @Override
        public void onFetchStart() {
            RequestQueueService.showProgressDialog(sbuView.getContext());
        }
    };
}
