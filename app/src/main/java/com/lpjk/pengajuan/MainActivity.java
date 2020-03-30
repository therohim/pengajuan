package com.lpjk.pengajuan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.fxn.pix.Pix;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lpjk.pengajuan.akun.AkunFragment;
import com.lpjk.pengajuan.akun.presenter.AkunPresenter;
import com.lpjk.pengajuan.akun.presenter.InterfaceAkunPresenter;
import com.lpjk.pengajuan.home.HomeFragment;
import com.lpjk.pengajuan.login.model.Login;
import com.lpjk.pengajuan.permohonan.PermohonanFragment;
import com.lpjk.pengajuan.utils.ServerUrl;
import com.rohim.rohimmodule.FetchDataListener;
import com.rohim.rohimmodule.ImageUtils;
import com.rohim.rohimmodule.RequestQueueService;
import com.rohim.rohimmodule.UserSession;
import com.rohim.rohimmodule.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView navigationView;
    private boolean doubleTapParam = false;
    String deviceToken="";
    UserSession session;
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment =null;
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(new HomeFragment());
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                try{
                    VolleyRequest postapiRequest=new VolleyRequest();
                    session = new UserSession(MainActivity.this);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", session.getSpUserId());
                        jsonObject.put("fcm_id", instanceIdResult.getToken());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    postapiRequest.request(MainActivity.this,updateFcm,jsonObject, ServerUrl.url_update_fcm_id,"POST");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    FetchDataListener updateFcm = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) throws JSONException {
        }

        @Override
        public void onFetchFailure(String msg) {
        }

        @Override
        public void onFetchStart() {

        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_permohonan:
                fragment = new PermohonanFragment();
                break;
            case R.id.nav_profile:
                fragment = new AkunFragment();
                break;
        }
        return loadFragment(fragment);
    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_konten, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 1001) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

            if (returnValue.size() > 0) {

                File f = new File(returnValue.get(0));
                Bitmap b = new BitmapDrawable(MainActivity.this.getResources(), f.getAbsolutePath()).getBitmap();
                saveFotoProfil(ImageUtils.convert(b));
            }
        }
    }

    public void saveFotoProfil(String foto) {
        session = new UserSession(MainActivity.this);
        try{
            VolleyRequest postapiRequest=new VolleyRequest();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", session.getSpUserId());
                jsonObject.put("foto_profil",foto);
            }catch (Exception e){
                e.printStackTrace();
            }
            postapiRequest.request(MainActivity.this,fetchUpdateFotoProfil,jsonObject, ServerUrl.url_update_foto_profil,"POST");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    FetchDataListener fetchUpdateFotoProfil = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) throws JSONException {
            Log.d(">>>>>",String.valueOf(data));
            RequestQueueService.cancelProgressDialog();
            JSONObject metadata= new JSONObject(data.optString("metadata"));
            int status = metadata.getInt("status");
            String message =metadata.getString("message");
            if(status == 200){
                fragment = new AkunFragment();
            }else{
                RequestQueueService.showAlertError(message,MainActivity.this);
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
//            RequestQueueService.showAlertError(msg,MainActivity.this);
        }

        @Override
        public void onFetchStart() {
            fragment = new AkunFragment();
//            RequestQueueService.showAlertError("Terjadi kesalahan",MainActivity.this);
//            RequestQueueService.showProgressDialog(MainActivity.this);
        }
    };

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
