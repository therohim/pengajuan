package com.lpjk.pengajuan.permohonan.skt.presenter;

import com.lpjk.pengajuan.permohonan.sbu.model.SbuModel;
import com.lpjk.pengajuan.permohonan.skt.model.SktModel;
import com.lpjk.pengajuan.permohonan.skt.view.SktView;
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

public class SktPresenter implements ISktPresenter {
    SktView view;
    UserSession session;
    List<SktModel> model;

    public SktPresenter(SktView view){
        this.view =view;
    }

    @Override
    public void getSkt() {
        session = new UserSession(view.getContext());
        try{
            VolleyRequest postapiRequest=new VolleyRequest();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id_user", session.getSpUserId());
            }catch (Exception e){
                e.printStackTrace();
            }
            postapiRequest.request(view.getContext(),fetchResultSkt,jsonObject, ServerUrl.url_get_skt,"POST");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private FetchDataListener fetchResultSkt = new FetchDataListener() {
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
                    model.add(new SktModel(
                            obj.getString("id"),
                            obj.getString("nik"),
                            obj.getString("nama"),
                            obj.getString("alamat"),
                            obj.getString("tgl_lahir"),
                            obj.getString("jenis_kelamin"),
                            obj.getString("tipe_permohonan"),
                            obj.getInt("status_approval"),
                            obj.getString("token_permohonan"),
                            obj.getString("token_klasifikasi")
                    ));
                    view.onGetResultSkt(model);
                }
            }else{
                RequestQueueService.showAlertError(message,view.getContext());
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlertError(msg,view.getContext());
        }

        @Override
        public void onFetchStart() {
            RequestQueueService.showProgressDialog(view.getContext());
        }
    };

}
