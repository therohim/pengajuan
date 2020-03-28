package com.lpjk.pengajuan.permohonan.skt.detail;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kinda.alert.KAlertDialog;
import com.lpjk.pengajuan.R;
import com.lpjk.pengajuan.permohonan.sbu.detail.DetailKlasifikasiAdapter;
import com.lpjk.pengajuan.permohonan.sbu.detail.DetailSbuActivity;
import com.lpjk.pengajuan.permohonan.sbu.model.DetailKlasifikasiModel;
import com.lpjk.pengajuan.permohonan.sbu.model.SbuModel;
import com.lpjk.pengajuan.permohonan.skt.model.SktModel;
import com.lpjk.pengajuan.utils.ServerUrl;
import com.lpjk.pengajuan.utils.Utils;
import com.ornach.nobobutton.NoboButton;
import com.rohim.rohimmodule.Alerty;
import com.rohim.rohimmodule.FetchDataListener;
import com.rohim.rohimmodule.RequestQueueService;
import com.rohim.rohimmodule.SignatureView;
import com.rohim.rohimmodule.UserSession;
import com.rohim.rohimmodule.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class DetailSktActivity extends AppCompatActivity {
    private SktModel skt;
    Toolbar toolbar;
    TextView tvNama, tvNik, tvAlamat, tvGender, tvTglLahir, tvTipe;
    RecyclerView rvDetailKlasifikasi;
    AlertDialog.Builder dialog;
    AlertDialog alDialog;
    LayoutInflater inflater;
    View dialogView;
    Bitmap bitmap;
    SignatureView signatureView;
    String path;

    private List<DetailKlasifikasiModel> detailKlasifikasiModelList = new ArrayList<>();
    private DetailKlasifikasiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_skt);
        if(getIntent().hasExtra("skt")){
            Gson gson = new Gson();
            skt = gson.fromJson(getIntent().getStringExtra("skt"), SktModel.class);
        }
        initUi();
        setupRecycler();
    }

    private void initUi(){
        toolbar = findViewById(R.id.toolbar);
        tvNama = findViewById(R.id.tv_nama);
        tvNik = findViewById(R.id.tv_nik);
        tvAlamat = findViewById(R.id.tv_alamat);
        tvGender = findViewById(R.id.tv_gender);
        tvTglLahir = findViewById(R.id.tv_tgl_lahir);
        tvTipe = findViewById(R.id.tv_tipe);
        rvDetailKlasifikasi = findViewById(R.id.rv_detail_klasifikasi);
        tvNik.setText(skt.getNik());
        tvNama.setText(skt.getNama());
        tvAlamat.setText(skt.getAlamat());
        tvGender.setText(skt.getJenis_kelamin());
        tvTipe.setText(skt.getTipe_permohonan());
        tvTglLahir.setText(skt.getTgl_lahir());
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_approval:
                if(skt.getStatus_approval() == 1){
                    new KAlertDialog(DetailSktActivity.this,KAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf!!....")
                            .setContentText("Anda sudah menyutujui personal "+skt.getNama())
                            .show();
                }else{
                    dialogApproval();
                }
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            VolleyRequest postapiRequest=new VolleyRequest();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", skt.getToken_klasifikasi());
            }catch (Exception e){
                e.printStackTrace();
            }
            postapiRequest.request(this,fetchDetailKlasifikasi,jsonObject, ServerUrl.url_detail_klasifikasi,"POST");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    FetchDataListener fetchDetailKlasifikasi = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) throws JSONException {
            RequestQueueService.cancelProgressDialog();
            JSONObject metadata= new JSONObject(data.optString("metadata"));
            Log.d(">>>>>",String.valueOf(metadata));
            int status = metadata.getInt("status");
            String message =metadata.getString("message");
            if(status == 200){
                JSONArray arr  = data.getJSONArray("response");
                Log.d(">>>>>>>", String.valueOf(arr));
                for (int i =0; i<arr.length(); i++){
                    JSONObject obj = arr.getJSONObject(i);
                    detailKlasifikasiModelList.add(new DetailKlasifikasiModel(
                            obj.getString("id"),
                            obj.getString("kode_klasifikasi"),
                            obj.getString("deskripsi_klasifikasi"),
                            obj.getString("deskripsi_lengkap_klasifikasi"),
                            obj.getString("kode_sub_klasifikasi"),
                            obj.getString("deskripsi_sub_klasifikasi"),
                            obj.getString("lingkup_pekerjaan_klasifikasi"),
                            obj.getString("kode_kualifikasi"),
                            obj.getString("total_biaya")
                    ));
                }
            }else {
                RequestQueueService.showAlertError(message, DetailSktActivity.this);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlertError(msg,DetailSktActivity.this);

        }

        @Override
        public void onFetchStart() {
            RequestQueueService.showProgressDialog(DetailSktActivity.this);
        }
    };

    private void setupRecycler(){
        adapter = new DetailKlasifikasiAdapter(this,detailKlasifikasiModelList);
        rvDetailKlasifikasi.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        rvDetailKlasifikasi.setLayoutManager(new LinearLayoutManager(this));
        rvDetailKlasifikasi.setAdapter(adapter);
    }

    private void dialogApproval() {
        dialog = new AlertDialog.Builder(this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_signature, null);
        signatureView =  dialogView.findViewById(R.id.layout_signature);
        NoboButton btnSave = dialogView.findViewById(R.id.btn_simpan);
        NoboButton btnCancel = dialogView.findViewById(R.id.btn_cancel);
        ImageView imgRefresh = dialogView.findViewById(R.id.img_refresh);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.lpjk);
        dialog.setTitle("Form Persetujuan");
        alDialog = dialog.show();

        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clearSignature();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alDialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap image = signatureView.getImage();
                UserSession session = new UserSession(DetailSktActivity.this);
                RequestQueueService.cancelProgressDialog();
                try{
                    VolleyRequest postapiRequest=new VolleyRequest();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id_user", session.getSpUserId());
                        jsonObject.put("id_personal", skt.getId());
                        jsonObject.put("ttd", Utils.getEncoded64ImageStringFromBitmap(image));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    postapiRequest.request(DetailSktActivity.this,processSkt,jsonObject, ServerUrl.url_process_skt,"POST");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    FetchDataListener processSkt = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) throws JSONException {
            JSONObject metadata = new JSONObject(data.optString("metadata"));
            int status = metadata.getInt("status");
            String message=metadata.getString("message");
            if(status==200){
                new KAlertDialog(DetailSktActivity.this, KAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Good job!")
                        .setContentText(message)
                        .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                alDialog.dismiss();
                                Intent intent = new Intent();
                                setResult(201, intent);
                                finish();
                            }
                        })
                        .show();
            }else{
                new Alerty(DetailSktActivity.this,Alerty.ERROR_DIALOG,message);
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlertError(msg,DetailSktActivity.this);

        }

        @Override
        public void onFetchStart() {
            RequestQueueService.showProgressDialog(DetailSktActivity.this);
        }
    };

}
