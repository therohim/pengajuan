package com.lpjk.pengajuan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lpjk.pengajuan.akun.AkunFragment;
import com.lpjk.pengajuan.home.HomeFragment;
import com.lpjk.pengajuan.login.model.Login;
import com.lpjk.pengajuan.permohonan.PermohonanFragment;
import com.lpjk.pengajuan.utils.ServerUrl;
import com.rohim.rohimmodule.FetchDataListener;
import com.rohim.rohimmodule.UserSession;
import com.rohim.rohimmodule.VolleyRequest;
import com.rohim.rohimmodule.toast.ToastBeautify;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView navigationView;
    private boolean doubleTapParam = false;
    String deviceToken="";
    UserSession session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Fragment fragment = null;
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
