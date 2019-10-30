package com.aliyanaresorts.aliyanahotelresorts.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.BookingTmpRooms;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.aliyanaresorts.aliyanahotelresorts.SplashActivity.MY_PERMISSIONS_REQUEST_GET_ACCESS;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_DOMAIN;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class Helper {

    public static void setViewStatus(int size, NestedScrollView nestedLayout, RelativeLayout kosongLayout) {
        if (size>0){
            nestedLayout.setVisibility(View.VISIBLE);
            kosongLayout.setVisibility(View.GONE);
        }else {
            nestedLayout.setVisibility(View.GONE);
            kosongLayout.setVisibility(View.VISIBLE);
        }
    }

    public static void closeKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void getPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_GET_ACCESS);
        }
    }

    public static String setTextData(String data){
        String text;
        if (data.equals("null")){
            text="";
        }else {
            text=data;
        }
        return text;
    }

    public static void setFotoUser(String foto, Context context, ImageView imageView){
        if (!foto.equals("null")){
            Glide.with(context).load(KEY_DOMAIN+foto)
                    .placeholder(R.drawable.image_slider_1)
                    .thumbnail(0.5f)
                    .centerCrop()
                    .transition(withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageView);
        }
    }

//    public static String convertPassMd5(String pass) {
//        String password = null;
//        MessageDigest mdEnc;
//        try {
//            mdEnc = MessageDigest.getInstance("MD5");
//            mdEnc.update(pass.getBytes(), 0, pass.length());
//            StringBuilder passBuilder = new StringBuilder(new BigInteger(1, mdEnc.digest()).toString(16));
//            while (passBuilder.length() < 32) {
//                passBuilder.insert(0, "0");
//            }
//            pass = passBuilder.toString();
//            password = pass;
//        } catch (NoSuchAlgorithmException e1) {
//            e1.printStackTrace();
//        }
//        return password;
//    }

    public static String formatingRupiah(String harga){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format((double)Double.valueOf(harga));
    }

    public static int getStatusBarHeight(Context context){
        return (int) Math.ceil(25 * context.getResources().getDisplayMetrics().density);
    }

    public static boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() == null || !connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public static String getIntentData(Activity activity,String nama){
        return activity.getIntent().getStringExtra(nama);
    }

    public static void setWarnaStatus(Activity activity, TextView status){
        String newString = status.toString();
        if (newString.equals("Payment Accepted")||newString.equals("Completed")){
            status.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        }else {
            status.setTextColor(activity.getResources().getColor(R.color.abang));
        }
    }

    public static void setWarnaButtonProses(Activity activity, String status, TextView button){
        if (status.equals("Payment Accepted")||status.equals("Completed")){
            button.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        }else {
            button.setBackgroundColor(activity.getResources().getColor(R.color.abang));
        }
    }

    public static String remTagHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
        }
    }

    public static int cekJmlKamar(int jml){
        if (jml%2==0){
            jml/=2;
        }else {
            jml-=1;
            jml/=2;
            jml+=1;
        }
        return jml;
    }

    public static int cekIdxTmp(ArrayList<BookingTmpRooms> arrayList, int posisi){
        int idx = -1;
        for(int i=0; i<arrayList.size(); i++){
            if(arrayList.get(i).getPosisi()==posisi){
                idx=i;
            }
        }
        return idx;
    }

    public static String getPhoneWithoutZeroFirst(String s){
        char[] phone = s.toCharArray();
        if (phone[0]=='0'){
            return s.substring(1);
        }else {
            return s;
        }
    }

}
