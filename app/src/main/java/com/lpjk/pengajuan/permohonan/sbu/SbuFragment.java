package com.lpjk.pengajuan.permohonan.sbu;


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
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.lpjk.pengajuan.R;
import com.lpjk.pengajuan.permohonan.sbu.model.SbuModel;
import com.lpjk.pengajuan.permohonan.sbu.presenter.SbuPresenter;
import com.lpjk.pengajuan.permohonan.sbu.view.SbuView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SbuFragment extends Fragment implements SbuView {

    private View view;
    private RecyclerView rvSbu;
    private SbuAdapter adapter;
    private List<SbuModel> sbuModels = new ArrayList<>();
    private SbuPresenter presenter;
    private MaterialRefreshLayout refreshSbu;

    public SbuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sbu, container, false);
        initUi();
        return view;
    }

    private void initUi(){
        presenter = new SbuPresenter(this);
        rvSbu = view.findViewById(R.id.rv_sbu);
        refreshSbu = view.findViewById(R.id.refresh);
        refreshSbu.setWaveColor(0xffffffff);
        refreshSbu.setIsOverLay(false);
        refreshSbu.setWaveShow(true);
        refreshSbu.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout refreshSbu) {
                refreshSbu.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        presenter.getSbu();
                        refreshSbu.finishRefresh();

                    }
                }, 3000);
                refreshSbu.finishRefreshLoadMore();
            }
            @Override
            public void onfinish() {
                presenter.getSbu();
            }
        });

        setupRecycler();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getSbu();
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
    public void onGetResultSbu(List<SbuModel> list) {
        sbuModels = list;
        adapter = new SbuAdapter(getActivity(),list);
        rvSbu.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorLoading(String message) {

    }

    @SuppressLint("WrongConstant")
    private void setupRecycler(){
        rvSbu.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SbuAdapter(getActivity(),sbuModels);
        rvSbu.setAdapter(adapter);
        rvSbu.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter.notifyDataSetChanged();
        rvSbu.setHasFixedSize(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 101){
//            presenter.getSbu();
//        }
    }
}
