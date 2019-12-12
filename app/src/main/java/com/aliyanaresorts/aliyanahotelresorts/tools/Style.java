package com.aliyanaresorts.aliyanahotelresorts.tools;

import android.app.Activity;
import android.os.Build;
//import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.aliyanaresorts.aliyanahotelresorts.R;

public class Style {

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    public static void setTemaAplikasi(Activity activity, int kode){
        if (kode==0){
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        }else {
            if (Build.VERSION.SDK_INT >= 23) {
                activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.putih));
            } else {
                activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.abu));
            }
        }
    }

    public static void setStyleStatusBarGoldTrans(Activity activity) {
        setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.goldtrans));
    }

    public static void setStyleStatusBarTransparent(Activity activity) {
        setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.transparan));
    }

//    public static void setWarnaToolbar(Activity activity, Toolbar toolbar){
//        if (Build.VERSION.SDK_INT >= 23) {
//            toolbar.setBackgroundColor(activity.getResources().getColor(R.color.putih));
//        } else {
//            toolbar.setBackgroundColor(activity.getResources().getColor(R.color.abu));
//        }
//    }

}
