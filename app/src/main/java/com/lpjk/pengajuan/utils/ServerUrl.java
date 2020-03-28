package com.lpjk.pengajuan.utils;

public class ServerUrl {
    static String base_url="http://lpjkjabar.net/pengajuan/api/";
//    static String base_url="http://192.168.20.86/project/pengajuan/api/";
//    static String base_url="http://192.168.43.204/project/pengajuan/api/";

    public static final String url_login = base_url+"Authentication/login";
    public static final String url_update_fcm_id = base_url+"Authentication/update_fcm";
    public static final String url_process_user = base_url+"Authentication/process_user";
    public static final String url_get_profile = base_url+"Authentication/get_profile";
    public static final String url_update_foto_profil = base_url+"Authentication/update_foto_profil";
    public static final String url_update_password = base_url+"Authentication/update_password";

    public static final String url_get_sbu = base_url+"Permohonan/sbu";
    public static final String url_get_skt = base_url+"Permohonan/skt";
    public static final String url_process_sbu = base_url+"Permohonan/process_sbu";
    public static final String url_process_skt = base_url+"Permohonan/process_skt";
    public static final String url_detail_klasifikasi = base_url+"Permohonan/detail_klasifikasi";

    public static final String url_get_total = base_url+"main/dashboard";
}