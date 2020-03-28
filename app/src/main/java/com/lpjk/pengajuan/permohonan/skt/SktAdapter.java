package com.lpjk.pengajuan.permohonan.skt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lpjk.pengajuan.R;
import com.lpjk.pengajuan.permohonan.sbu.SbuAdapter;
import com.lpjk.pengajuan.permohonan.sbu.detail.DetailSbuActivity;
import com.lpjk.pengajuan.permohonan.sbu.model.SbuModel;
import com.lpjk.pengajuan.permohonan.skt.detail.DetailSktActivity;
import com.lpjk.pengajuan.permohonan.skt.model.SktModel;

import java.util.ArrayList;
import java.util.List;

public class SktAdapter extends RecyclerView.Adapter<SktAdapter.ViewHolder> {

    private List<SktModel> models = new ArrayList<>();
    private Activity context;

    public SktAdapter(List<SktModel> models, Activity context){
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public SktAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skt,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SktAdapter.ViewHolder holder, int position) {
        final SktModel dt = models.get(position);
        holder.tvNama.setText(dt.getNama());
        holder.tvAlamat.setText(dt.getAlamat());
        holder.tvTipe.setText(dt.getTipe_permohonan().toUpperCase());
        holder.tvJk.setText(dt.getJenis_kelamin());
        if(dt.getStatus_approval() == 1){
            holder.tvStatus.setText("Sudah diapproval");
            holder.rlStatus.setBackgroundResource(R.drawable.bg_sudah);
        }else{
            holder.tvStatus.setText("Belum diapproval");
            holder.rlStatus.setBackgroundResource(R.drawable.bg_belum);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent intent = new Intent(context, DetailSktActivity.class);
                intent.putExtra("skt", gson.toJson(dt));
                context.startActivityForResult(intent,201);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvAlamat, tvStatus, tvTipe, tvJk;
        RelativeLayout rlStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAlamat = itemView.findViewById(R.id.tv_alamat);
            tvTipe = itemView.findViewById(R.id.tv_tipe);
            tvJk = itemView.findViewById(R.id.tv_jk);
            tvStatus = itemView.findViewById(R.id.tv_status);
            rlStatus = itemView.findViewById(R.id.rl_status);
        }
    }
}
