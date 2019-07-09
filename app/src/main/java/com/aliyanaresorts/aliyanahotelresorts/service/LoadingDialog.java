package com.aliyanaresorts.aliyanahotelresorts.service;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.bumptech.glide.Glide;

import java.util.Objects;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class LoadingDialog {

    private final Activity activity;
    private Dialog dialog;

    public LoadingDialog(Activity activity){
        this.activity = activity;
    }

    public void bukaDialog(){
        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.color.transparan);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_loading);

        ImageView gifImageView = dialog.findViewById(R.id.loadingView);

        Glide.with(activity)
                .load(R.drawable.anim_loading)
                .fitCenter()
                .transition(withCrossFade())
                .into(gifImageView);
        dialog.show();
    }

    public void tutupDialog(){
        dialog.dismiss();
    }

}
