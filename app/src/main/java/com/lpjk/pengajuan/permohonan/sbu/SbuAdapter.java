package com.lpjk.pengajuan.permohonan.sbu;

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
import com.lpjk.pengajuan.permohonan.sbu.detail.DetailSbuActivity;
import com.lpjk.pengajuan.permohonan.sbu.model.SbuModel;

import java.util.ArrayList;
import java.util.List;

public class AdapterSbu extends RecyclerView.Adapter<AdapterSbu.ViewHolder> {
    private List<SbuModel> models = new ArrayList<>();
    private Activity context;

    public AdapterSbu(Activity context, List<SbuModel> models){
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterSbu.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sbu,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSbu.ViewHolder holder, int position) {
        final SbuModel dt = models.get(position);
        holder.tvNpwp.setText(dt.getNpwp());
        holder.tvNama.setText(dt.getNama());
        holder.tvAlamat.setText(dt.getAlamat());
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
                Intent intent = new Intent(context, DetailSbuActivity.class);
                intent.putExtra("sbu", gson.toJson(dt));
                context.startActivityForResult(intent,101);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNpwp, tvNama, tvAlamat, tvStatus;
        RelativeLayout rlStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNpwp = itemView.findViewById(R.id.tv_npwp);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAlamat = itemView.findViewById(R.id.tv_alamat);
            tvStatus = itemView.findViewById(R.id.tv_status);
            rlStatus = itemView.findViewById(R.id.rl_status);
        }
    }
}
