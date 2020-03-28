package com.lpjk.pengajuan.akun;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lpjk.pengajuan.R;
import com.lpjk.pengajuan.akun.presenter.AkunPresenter;
import com.lpjk.pengajuan.akun.presenter.InterfaceAkunPresenter;
import com.lpjk.pengajuan.akun.view.InterfaceAkunView;
import com.lpjk.pengajuan.login.LoginActivity;
import com.rohim.rohimmodule.Alerty;
import com.rohim.rohimmodule.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AkunFragment extends Fragment implements InterfaceAkunView {

    View view;
    TextView tvNama, tvAlamat, tvKontak;
    ImageView imgProfile, imgPass;
    RelativeLayout rlLogut;
    UserSession session;
    InterfaceAkunPresenter interfaceAkunPresenter;
    Toolbar toolbar;

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
