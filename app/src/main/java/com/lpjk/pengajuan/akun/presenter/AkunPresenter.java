package com.lpjk.pengajuan.akun.presenter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lpjk.pengajuan.MainActivity;
import com.lpjk.pengajuan.R;
import com.lpjk.pengajuan.akun.view.InterfaceAkunView;
import com.lpjk.pengajuan.utils.ServerUrl;
import com.rohim.rohimmodule.FetchDataListener;
import com.rohim.rohimmodule.RequestQueueService;
import com.rohim.rohimmodule.UserSession;
import com.rohim.rohimmodule.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class AkunPresenter implements InterfaceAkunPresenter {
    private InterfaceAkunView interfaceAkunView;
    private UserSession session;
    private EditText edtPassBaru, edtPassLama, edtNik, edtNama, edtAlamat, edtNoTelp, edtEmail;
    private Button btnSave, btnCancel;
    private BottomSheetDialog dialogEdtPass, dialogEdtrofil;

    public AkunPresenter(InterfaceAkunView interfaceAkunView){
        this.interfaceAkunView = interfaceAkunView;
    }

    @Override
    public void editProfil() {

        View view = interfaceAkunView.getContext().getLayoutInflater().inflate(R.layout.bottom_sheet_update_profil, null);
        dialogEdtrofil = new BottomSheetDialog(interfaceAkunView.getContext());
        dialogEdtrofil.setContentView(view);
        dialogEdtrofil.show();
        edtNama =view.findViewById(R.id.edt_nama);
        edtAlamat = view.findViewById(R.id.edt_alamat);
        edtNoTelp = view.findViewById(R.id.edt_kontak);
        edtEmail = view.findViewById(R.id.edt_email);
        btnSave = view.findViewById(R.id.btn_simpan);
        btnCancel = view.findViewById(R.id.btn_cancel);
        session = new UserSession(interfaceAkunView.getContext());
        try{
            VolleyRequest postapiRequest=new VolleyRequest();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", session.getSpUserId());
            }catch (Exception e){
                e.printStackTrace();
            }
            postapiRequest.request(interfaceAkunView.getContext(),profil,jsonObject, ServerUrl.url_get_profile,"POST");
        }catch (Exception e){
            e.printStackTrace();
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEdtrofil.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {try {
                VolleyRequest request = new VolleyRequest();
                JSONObject object = new JSONObject();
                try {
                    object.put("id",session.getSpUserId());
                    object.put("nama",edtNama.getText().toString());
                    object.put("alamat",edtAlamat.getText().toString());
                    object.put("no_telp",edtNoTelp.getText().toString());
                    object.put("email",edtEmail.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                request.request(interfaceAkunView.getContext(),updateProfil,object,ServerUrl.url_process_user,"POST");
            }catch (Exception e){
                e.printStackTrace();
            }
            }
        });
    }

    private FetchDataListener profil  = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) throws JSONException {
            RequestQueueService.cancelProgressDialog();
            JSONObject metadata= new JSONObject(data.optString("metadata"));
            int status = metadata.getInt("status");
            String message =metadata.getString("message");
            if(status == 200){
                JSONObject response = new JSONObject(data.optString("response"));
                edtNama.setText(response.getString("nama"));
                edtAlamat.setText(response.getString("alamat"));
                edtNoTelp.setText(response.getString("kontak"));
                edtEmail.setText(response.getString("email"));
            }else{
                RequestQueueService.showAlertError(message,interfaceAkunView.getContext());
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlertError(msg,interfaceAkunView.getContext());
        }

        @Override
        public void onFetchStart() {
            RequestQueueService.showProgressDialog(interfaceAkunView.getContext());
        }
    };

    private FetchDataListener updateProfil = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) throws JSONException {
            JSONObject meta = new JSONObject(data.optString("metadata"));
            RequestQueueService.cancelProgressDialog();
            if(meta.getInt("status")==200){
                interfaceAkunView.onUpdateProfilSuccess(meta.getString("message"));
                getProfil();
//                JSONObject response = new JSONObject(data.optString("response"));
//                interfaceAkunView.getDataProfil(response);
                dialogEdtrofil.dismiss();
            }else{
                interfaceAkunView.onUpdateProfilError(meta.getString("message"));
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlertError(msg,interfaceAkunView.getContext());
        }

        @Override
        public void onFetchStart() {
            RequestQueueService.showProgressDialog(interfaceAkunView.getContext());
        }
    };

    @Override
    public void editPassword() {
        View view = interfaceAkunView.getContext().getLayoutInflater().inflate(R.layout.bottom_sheet_update_pass, null);
        dialogEdtPass = new BottomSheetDialog(interfaceAkunView.getContext());
        dialogEdtPass.setContentView(view);
        dialogEdtPass.show();
        edtPassLama = view.findViewById(R.id.edt_pass_lama);
        edtPassBaru = view.findViewById(R.id.edt_pass_baru);
        btnSave = view.findViewById(R.id.btn_simpan);
        btnCancel = view.findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session = new UserSession(interfaceAkunView.getContext());
                try {
                    VolleyRequest request = new VolleyRequest();
                    JSONObject object = new JSONObject();
                    try {
                        object.put("id",session.getSpUserId());
                        object.put("password_lama",edtPassLama.getText().toString());
                        object.put("password_baru",edtPassBaru.getText().toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    request.request(interfaceAkunView.getContext(),updatePass,object,ServerUrl.url_update_password,"POST");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEdtPass.dismiss();
            }
        });
    }
    FetchDataListener updatePass = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) throws JSONException {
            RequestQueueService.cancelProgressDialog();
            JSONObject metadata= new JSONObject(data.optString("metadata"));
            int status = metadata.getInt("status");
            String message =metadata.getString("message");
            if(status == 200){
                interfaceAkunView.onUpdatePassSuccess(message);
                dialogEdtPass.dismiss();
            }else{
                interfaceAkunView.onUpdatePassError(message);
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlertError(msg,interfaceAkunView.getContext());
        }

        @Override
        public void onFetchStart() {
            RequestQueueService.showProgressDialog(interfaceAkunView.getContext());

        }
    };

    @Override
    public void getProfil() {
        session = new UserSession(interfaceAkunView.getContext());
        try{
            VolleyRequest postapiRequest=new VolleyRequest();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", session.getSpUserId());
            }catch (Exception e){
                e.printStackTrace();
            }
            postapiRequest.request(interfaceAkunView.getContext(),getDataProfil,jsonObject, ServerUrl.url_get_profile,"POST");
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
                Log.d(">>>>>",String.valueOf(response));
                interfaceAkunView.getDataProfil(response);
            }else{
                RequestQueueService.showAlertError(message,interfaceAkunView.getContext());
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlertError("Terjadi kesalahan",interfaceAkunView.getContext());
        }

        @Override
        public void onFetchStart() {
            RequestQueueService.showAlertError("Terjadi kesalahan",interfaceAkunView.getContext());
            RequestQueueService.cancelProgressDialog();
        }
    };

//    @Override
//    public void saveFotoProfil(String foto) {
//        session = new UserSession(interfaceAkunView.getContext());
//        try{
//            VolleyRequest postapiRequest=new VolleyRequest();
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("id", session.getSpUserId());
//                jsonObject.put("foto_profil",foto);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            postapiRequest.request(interfaceAkunView.getContext(),fetchUpdateFotoProfil,jsonObject, ServerUrl.url_update_foto_profil,"POST");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    FetchDataListener fetchUpdateFotoProfil = new FetchDataListener() {
//        @Override
//        public void onFetchComplete(JSONObject data) throws JSONException {
//            RequestQueueService.cancelProgressDialog();
//            Log.d(">>>>",String.valueOf(data));
//            JSONObject metadata= new JSONObject(data.optString("metadata"));
//            int status = metadata.getInt("status");
//            String message =metadata.getString("message");
//            if(status == 200){
//                JSONObject response = new JSONObject(data.optString("response"));
//                interfaceAkunView.getDataProfil(response);
//            }else{
//                RequestQueueService.showAlertError(message,interfaceAkunView.getContext());
//            }
//        }
//
//        @Override
//        public void onFetchFailure(String msg) {
//            RequestQueueService.cancelProgressDialog();
//            RequestQueueService.showAlertError(msg,interfaceAkunView.getContext());
//        }
//
//        @Override
//        public void onFetchStart() {
//            RequestQueueService.showProgressDialog(interfaceAkunView.getContext());
//        }
//    };

}
