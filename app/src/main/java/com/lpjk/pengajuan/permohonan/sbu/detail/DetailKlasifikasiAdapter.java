package com.lpjk.pengajuan.permohonan.sbu.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lpjk.pengajuan.R;
import com.lpjk.pengajuan.permohonan.sbu.model.DetailKlasifikasiModel;
import com.lpjk.pengajuan.permohonan.sbu.model.SbuModel;

import java.util.ArrayList;
import java.util.List;

import static com.lpjk.pengajuan.utils.Utils.rupiah;

public class DetailKlasifikasiAdapter extends RecyclerView.Adapter<DetailKlasifikasiAdapter.ViewHolder> {
    private List<DetailKlasifikasiModel> models = new ArrayList<>();
    private Activity context;

    public DetailKlasifikasiAdapter(Activity context, List<DetailKlasifikasiModel> models){
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public DetailKlasifikasiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_klasifikasi,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull DetailKlasifikasiAdapter.ViewHolder holder, int position) {
        DetailKlasifikasiModel k = models.get(position);
        holder.tvDeskripsiKlasifikasi.setText(k.getDeskripsi_klasifikasi());
        holder.tvDeskripsiSubKlasifikasi.setText(k.getDeskripsi_sub_klasifikasi());
        holder.tvKodeKualifikasi.setText(k.getKode_kualifikasi());
        holder.tvBiaya.setText(rupiah(Double.parseDouble(k.getTotal_biaya())));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeskripsiKlasifikasi, tvDeskripsiSubKlasifikasi, tvKodeKualifikasi, tvBiaya;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeskripsiKlasifikasi = itemView.findViewById(R.id.tv_deskripsi_klasifikasi);
            tvDeskripsiSubKlasifikasi = itemView.findViewById(R.id.tv_deskripsi_sub_klasifikasi);
            tvKodeKualifikasi = itemView.findViewById(R.id.tv_kode_kualifikasi);
            tvBiaya = itemView.findViewById(R.id.tv_total_biaya);
        }
    }
}
