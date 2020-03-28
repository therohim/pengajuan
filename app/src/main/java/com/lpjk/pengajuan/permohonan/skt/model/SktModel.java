package com.lpjk.pengajuan.permohonan.skt.model;

public class SktModel {
    String id, nik, nama, alamat, tgl_lahir, jenis_kelamin, tipe_permohonan ,token_permohonan, token_klasifikasi;
    int status_approval;

    public SktModel(String id, String nik, String nama, String alamat, String tgl_lahir, String jenis_kelamin, String tipe_permohonan, int status_approval,String token_permohonan, String token_klasifikasi){
        this.id = id;
        this.nik = nik;
        this.nama = nama;
        this.alamat = alamat;
        this.tgl_lahir = tgl_lahir;
        this.jenis_kelamin = jenis_kelamin;
        this.tipe_permohonan = tipe_permohonan;
        this.status_approval = status_approval;
        this.token_permohonan = token_permohonan;
        this.token_klasifikasi = token_klasifikasi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
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

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public void setTgl_lahir(String tgl_lahir) {
        this.tgl_lahir = tgl_lahir;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getTipe_permohonan() {
        return tipe_permohonan;
    }

    public void setTipe_permohonan(String tipe_permohonan) {
        this.tipe_permohonan = tipe_permohonan;
    }

    public int getStatus_approval() {
        return status_approval;
    }

    public void setStatus_approval(int status_approval) {
        this.status_approval = status_approval;
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
}
