package com.aliyanaresorts.aliyanahotelresorts.service;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

import com.aliyanaresorts.aliyanahotelresorts.R;

import java.util.Objects;

public class NoInetDialog {

    private final Activity activity;
    private Dialog dialog;

    public NoInetDialog(Activity activity){
        this.activity = activity;
    }

    public void bukaDialog(){
        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.color.transparan);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_tidak_inet);
        dialog.show();
    }

    public void tutupDialog(){
        dialog.dismiss();
    }

}
