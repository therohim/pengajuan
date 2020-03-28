package com.lpjk.pengajuan.akun.model;

public class Akun {
    String id,nama,alamat,email,telp;

    public Akun(String id, String nama, String alamat, String email, String telp){
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.email = email;
        this.telp = telp;
    }
    public Akun(String id, String nama, String alamat, String email){
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.email = email;
    }
    public Akun(String id, String nama, String alamat){
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

}
