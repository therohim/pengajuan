package com.rohim.rohimmodule;

import android.content.Context;
import android.graphics.Color;

import com.kinda.alert.KAlertDialog;

public class AlertDialog {
    public static final int PROGRESS_DIALOG =1;
    public static final int SUCCESS_DIALOG =2;
    public static final int ERROR_DIALOG = 3;
    public static final int WARNING_DIALOG = 4;
    private static KAlertDialog mProgressDialog;
    public AlertDialog(Context context, int type, String message){
        if(type == PROGRESS_DIALOG){
            mProgressDialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
            mProgressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            mProgressDialog.setTitleText("Loading");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }else if(type == SUCCESS_DIALOG){
            mProgressDialog =new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE);
            mProgressDialog.setTitleText("Good job!");
            mProgressDialog.setContentText(message);
            mProgressDialog.show();
        }else if(type == ERROR_DIALOG){
            mProgressDialog =new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE);
            mProgressDialog.setTitleText("Ooops..");
            mProgressDialog.setContentText(message);
            mProgressDialog.show();
        }else if(type == WARNING_DIALOG){
            mProgressDialog =new KAlertDialog(context, KAlertDialog.WARNING_TYPE);
            mProgressDialog.setTitleText("Are you sure?");
            mProgressDialog.setContentText(message);
            mProgressDialog.setConfirmText("Yes");
            mProgressDialog.show();
        }
    }
}
