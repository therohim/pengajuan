package com.lpjk.pengajuan.akun;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.ImageQuality;
import com.lpjk.pengajuan.MainActivity;
import com.lpjk.pengajuan.R;
import com.lpjk.pengajuan.akun.presenter.AkunPresenter;
import com.lpjk.pengajuan.akun.presenter.InterfaceAkunPresenter;
import com.lpjk.pengajuan.akun.view.InterfaceAkunView;
import com.lpjk.pengajuan.login.LoginActivity;
import com.rohim.rohimmodule.Alerty;
import com.rohim.rohimmodule.CircleTransform;
import com.rohim.rohimmodule.ImageUtils;
import com.rohim.rohimmodule.UserSession;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AkunFragment extends Fragment implements InterfaceAkunView {

    View view;
    TextView tvNama, tvAlamat, tvKontak;
    ImageView imgProfile, imgPass, imgCamera;
    RelativeLayout rlLogut;
    UserSession session;
    InterfaceAkunPresenter interfaceAkunPresenter;
    Toolbar toolbar;
    CircleImageView ivProfilMain;

    public AkunFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_akun, container, false);
        session = new UserSession(getContext());
        interfaceAkunPresenter = new AkunPresenter(this);
        interfaceAkunPresenter.getProfil();
        initUi();
        return view;
    }

    private void initUi(){
        toolbar = view.findViewById(R.id.toolbar);
        tvNama = view.findViewById(R.id.tv_nama);
        tvAlamat = view.findViewById(R.id.tv_alamat);
        tvKontak = view.findViewById(R.id.tv_kontak);
        imgProfile = view.findViewById(R.id.iv_profile);
        imgPass = view.findViewById(R.id.iv_pass);
        imgCamera = view.findViewById(R.id.iv_image);
        ivProfilMain = view.findViewById(R.id.iv_profile_main);
        rlLogut = view.findViewById(R.id.rl_logout);

        toolbar.setTitle("Profil");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        imgPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceAkunPresenter.editPassword();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceAkunPresenter.editProfil();
            }
        });

        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pixImage();
            }
        });

        rlLogut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.saveSPBoolean(UserSession.SP_SUDAH_LOGIN, false);
                startActivity(new Intent(getContext(), LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().finish();
            }
        });
    }

    private void pixImage(){
        Options options = Options.init()
                .setRequestCode(1001)                                                //Request code for activity results
                .setCount(1)                                                         //Number of images to restict selection count
                .setFrontfacing(false)                                               //Front Facing camera on start
                .setImageQuality(ImageQuality.HIGH)                                  //Image Quality
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)           //Orientaion
                .setPath("/absensigmedia/images");                                             //Custom Path For Image Storage

        Pix.start((MainActivity) getContext(), options);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 1001) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

            if (returnValue.size() > 0) {

                File f = new File(returnValue.get(0));
                Bitmap b = new BitmapDrawable(getContext().getResources(), f.getAbsolutePath()).getBitmap();
                interfaceAkunPresenter.saveFotoProfil(ImageUtils.convert(b));
            }
        }
    }

    @Override
    public void onUpdateProfilSuccess(String message) {
        new Alerty(getContext(), Alerty.SUCCESS_DIALOG, message);
    }

    @Override
    public void onUpdateProfilError(String message) {
        new Alerty(getContext(), Alerty.ERROR_DIALOG, message);
    }

    @Override
    public void onUpdatePassSuccess(String message) {
        new Alerty(getContext(), Alerty.SUCCESS_DIALOG, message);

    }

    @Override
    public void onUpdatePassError(String message) {
        new Alerty(getContext(), Alerty.ERROR_DIALOG, message);
    }

    @Override
    public void End() {
        getActivity().finish();
    }

    @Override
    public Activity getContext() {
        return getActivity();
    }

    @Override
    public void getDataProfil(JSONObject jsonObject) {
        try {
            tvNama.setText(jsonObject.getString("nama"));
            tvAlamat.setText(jsonObject.getString("alamat"));
            tvKontak.setText(jsonObject.getString("kontak"));
            if(!jsonObject.getString("foto").equals("")){
                Picasso.get()
                        .load(jsonObject.getString("foto"))
                        .into(ivProfilMain);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
