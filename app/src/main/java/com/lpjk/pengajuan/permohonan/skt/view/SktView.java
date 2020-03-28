package com.lpjk.pengajuan.permohonan.skt.view;

import android.content.Context;
import android.view.LayoutInflater;

import com.lpjk.pengajuan.permohonan.sbu.model.SbuModel;
import com.lpjk.pengajuan.permohonan.skt.model.SktModel;

import java.util.List;

public interface SktView {
    void End();
    Context getContext();
    LayoutInflater Inflater();
    void onGetResultSkt(List<SktModel> list);
    void onErrorLoading(String message);
}
