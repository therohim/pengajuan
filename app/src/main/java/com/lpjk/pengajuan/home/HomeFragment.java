package com.lpjk.pengajuan.home;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lpjk.pengajuan.R;
import com.lpjk.pengajuan.home.presenter.HomePresenter;
import com.lpjk.pengajuan.home.presenter.InterfaceHomePresenter;
import com.lpjk.pengajuan.home.view.InterfaceHomeView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements InterfaceHomeView {

    View view;
    TextView tvSbu, tvSka, tvSkt, tvNama, tvNIK;
    InterfaceHomePresenter presenter;
    ImageView imgUser;
//    Toolbar toolbar;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        presenter = new HomePresenter(this);
        initUi();
        return view;
    }

    private void initUi(){
        tvSbu = view.findViewById(R.id.tv_sbu);
        tvSka = view.findViewById(R.id.tv_ska);
        tvSkt = view.findViewById(R.id.tv_skt);
        tvNama = view.findViewById(R.id.tv_nama);
        tvNIK = view.findViewById(R.id.tv_nik);
        imgUser = view.findViewById(R.id.user_photo);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getTotalDashboard();
        presenter.getProfil();
    }

    @Override
    public Activity getContext(){
        return getActivity();
    }

    @Override
    public void onTotal(JSONObject jsonObject) {
        try {
            tvSbu.setText(jsonObject.getString("jml_sbu"));
            tvSka.setText(jsonObject.getString("jml_ska"));
            tvSkt.setText(jsonObject.getString("jml_skt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProfil(String nama, String nik, String foto) {
        tvNIK.setText(nik);
        tvNama.setText(nama);
        if(!foto.equals("")){
            Picasso.get()
                    .load(foto)
                    .placeholder(R.drawable.user_red2)
                    .into(imgUser);
        }
    }

    @Override
    public void End() {
        getActivity().finish();
    }

}
