package com.lpjk.pengajuan.permohonan.sbu.view;

import android.content.Context;
import android.view.LayoutInflater;

import com.lpjk.pengajuan.permohonan.sbu.model.SbuModel;

import java.util.List;

public interface SbuView {
    void End();
    Context getContext();
    LayoutInflater Inflater();
    void onGetResultSbu(List<SbuModel> list);
    void onErrorLoading(String message);
}
