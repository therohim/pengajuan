package com.lpjk.pengajuan.permohonan.sbu.model;

public class DetailKlasifikasiModel {
    String id,kode_klasifikasi,deskripsi_klasifikasi,deskripsi_lengkap_klasifikasi,kode_sub_klasifikasi,
            deskripsi_sub_klasifikasi,lingkup_pekerjaan_klasifikasi,kode_kualifikasi,total_biaya;

    public DetailKlasifikasiModel(String id,String kode_klasifikasi,String deskripsi_klasifikasi,String deskripsi_lengkap_klasifikasi,String kode_sub_klasifikasi,String deskripsi_sub_klasifikasi,String lingkup_pekerjaan_klasifikasi,String kode_kualifikasi,String total_biaya){
        this.id =id;
        this.kode_klasifikasi =kode_klasifikasi;
        this.deskripsi_klasifikasi = deskripsi_klasifikasi;
        this.deskripsi_lengkap_klasifikasi = deskripsi_lengkap_klasifikasi;
        this.kode_sub_klasifikasi = kode_sub_klasifikasi;
        this.deskripsi_sub_klasifikasi = deskripsi_sub_klasifikasi;
        this.lingkup_pekerjaan_klasifikasi = lingkup_pekerjaan_klasifikasi;
        this.kode_kualifikasi = kode_kualifikasi;
        this.total_biaya = total_biaya;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKode_klasifikasi() {
        return kode_klasifikasi;
    }

    public void setKode_klasifikasi(String kode_klasifikasi) {
        this.kode_klasifikasi = kode_klasifikasi;
    }

    public String getDeskripsi_klasifikasi() {
        return deskripsi_klasifikasi;
    }

    public void setDeskripsi_klasifikasi(String deskripsi_klasifikasi) {
        this.deskripsi_klasifikasi = deskripsi_klasifikasi;
    }

    public String getDeskripsi_lengkap_klasifikasi() {
        return deskripsi_lengkap_klasifikasi;
    }

    public void setDeskripsi_lengkap_klasifikasi(String deskripsi_lengkap_klasifikasi) {
        this.deskripsi_lengkap_klasifikasi = deskripsi_lengkap_klasifikasi;
    }

    public String getKode_sub_klasifikasi() {
        return kode_sub_klasifikasi;
    }

    public void setKode_sub_klasifikasi(String kode_sub_klasifikasi) {
        this.kode_sub_klasifikasi = kode_sub_klasifikasi;
    }

    public String getDeskripsi_sub_klasifikasi() {
        return deskripsi_sub_klasifikasi;
    }

    public void setDeskripsi_sub_klasifikasi(String deskripsi_sub_klasifikasi) {
        this.deskripsi_sub_klasifikasi = deskripsi_sub_klasifikasi;
    }

    public String getLingkup_pekerjaan_klasifikasi() {
        return lingkup_pekerjaan_klasifikasi;
    }

    public void setLingkup_pekerjaan_klasifikasi(String lingkup_pekerjaan_klasifikasi) {
        this.lingkup_pekerjaan_klasifikasi = lingkup_pekerjaan_klasifikasi;
    }

    public String getKode_kualifikasi() {
        return kode_kualifikasi;
    }

    public void setKode_kualifikasi(String kode_kualifikasi) {
        this.kode_kualifikasi = kode_kualifikasi;
    }

    public String getTotal_biaya() {
        return total_biaya;
    }

    public void setTotal_biaya(String total_biaya) {
        this.total_biaya = total_biaya;
    }
}