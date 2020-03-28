package com.lpjk.pengajuan.permohonan.skt;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.lpjk.pengajuan.R;
import com.lpjk.pengajuan.permohonan.sbu.SbuAdapter;
import com.lpjk.pengajuan.permohonan.sbu.model.SbuModel;
import com.lpjk.pengajuan.permohonan.sbu.presenter.SbuPresenter;
import com.lpjk.pengajuan.permohonan.skt.model.SktModel;
import com.lpjk.pengajuan.permohonan.skt.presenter.SktPresenter;
import com.lpjk.pengajuan.permohonan.skt.view.SktView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SktFragment extends Fragment implements SktView {

    View view;
    private RecyclerView rvSkt;
    private SktAdapter adapter;
    private List<SktModel> sktModels = new ArrayList<>();
    private SktPresenter presenter;
    private MaterialRefreshLayout refreshSkt;

    public SktFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_skt, container, false);
        initUi();
        return view;
    }

    private void initUi(){
        presenter = new SktPresenter(this);
        rvSkt = view.findViewById(R.id.rv_skt);
        refreshSkt = view.findViewById(R.id.refresh_skt);
        refreshSkt.setWaveColor(0xffffffff);
        refreshSkt.setIsOverLay(false);
        refreshSkt.setWaveShow(true);
        refreshSkt.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout refreshSkt) {
                refreshSkt.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshSkt.finishRefresh();
                    }
                }, 3000);
                refreshSkt.finishRefreshLoadMore();
            }
            @Override
            public void onfinish() {
                presenter.getSkt();
            }
        });
        setupRecycler();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getSkt();
    }

    @Override
    public Context getContext(){
        return getActivity();
    }


    @Override
    public void End() {
        getActivity().finish();
    }

    @Override
    public LayoutInflater Inflater() {
        return getActivity().getLayoutInflater();
    }

    @Override
    public void onGetResultSkt(List<SktModel> list) {
        sktModels = list;
        adapter = new SktAdapter(list, getActivity());
        rvSkt.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorLoading(String message) {

    }

    @SuppressLint("WrongConstant")
    private void setupRecycler(){
        rvSkt.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SktAdapter(sktModels, getActivity());
        rvSkt.setAdapter(adapter);
        rvSkt.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter.notifyDataSetChanged();
        rvSkt.setHasFixedSize(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 201){
//            presenter.getSkt();
//        }
    }
}
