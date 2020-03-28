package com.lpjk.pengajuan.permohonan.sbu.model;

public class SbuModel {
    private String npwp, nama, alamat, kota, kbli, token_permohonan, token_klasifikasi;
    private int id,status_approval;

    public SbuModel(int id, String npwp, String nama, String alamat, String kota, String kbli, String token_permohonan, String token_klasifikasi, int status_approval){
        this.id = id;
        this.npwp = npwp;
        this.nama = nama;
        this.alamat = alamat;
        this.kota = kota;
        this.kbli = kbli;
        this.token_permohonan =token_permohonan;
        this.token_klasifikasi =token_klasifikasi;
        this.status_approval = status_approval;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNpwp() {
        return npwp;
    }

    public void setNpwp(String npwp) {
        this.npwp = npwp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getKbli() {
        return kbli;
    }

    public void setKbli(String kbli) {
        this.kbli = kbli;
    }

    public String getToken_permohonan() {
        return token_permohonan;
    }

    public void setToken_permohonan(String token_permohonan) {
        this.token_permohonan = token_permohonan;
    }

    public String getToken_klasifikasi() {
        return token_klasifikasi;
    }

    public void setToken_klasifikasi(String token_klasifikasi) {
        this.token_klasifikasi = token_klasifikasi;
    }

    public int getStatus_approval() {
        return status_approval;
    }

    public void setStatus_approval(int status_approval) {
        this.status_approval = status_approval;
    }
}
