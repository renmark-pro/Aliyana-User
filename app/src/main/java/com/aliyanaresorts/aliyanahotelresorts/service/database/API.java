package com.aliyanaresorts.aliyanahotelresorts.service.database;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class API {

    public static final String KEY_DOMAIN = "http://sistem.aliyanaresorts.com";
//    public static final String KEY_KAMAR_LIST="http://aliyanaresorts.com/app/kamarData.php";
    public static final String KEY_KAMAR_LIST="http://sistem.aliyanaresorts.com/public/api/apps/kamar";
//    public static final String KEY_FASILITAS_LIST="http://aliyanaresorts.com/app/fasilitasList.php";
    public static final String KEY_FASILITAS_LIST="http://sistem.aliyanaresorts.com/api/apps/fasilitas";
//    public static final String KEY_KAMAR_DETAIL="http://aliyanaresorts.com/app/kamarDetail.php?id=";
    public static final String KEY_KAMAR_DETAIL="http://sistem.aliyanaresorts.com/public/api/apps/kamar/";
//    public static final String KEY_FASILITAS_DETAIL="http://aliyanaresorts.com/app/fasilitasDetail.php?id=";
    public static final String KEY_FASILITAS_DETAIL="http://sistem.aliyanaresorts.com/api/apps/fasilitas/";
    public static final String KEY_PROMO_LIST="http://aliyanaresorts.com/app/promoList.php";
    public static final String KEY_SLIDE_HOME="http://aliyanaresorts.com/app/slideHome.php";
    public static final String KEY_TENTANG_HOTEL="http://aliyanaresorts.com/app/tentangHotel.php?id=1";
    public static final String KEY_MASUK="http://aliyanaresorts.com/app/user/Masuk.php";
    public static final String KEY_DAFTAR="http://aliyanaresorts.com/app/user/Daftar.php";
    public static final String KEY_UPDATE="http://aliyanaresorts.com/app/user/Update.php";
    public static final String KEY_BUG="http://aliyanaresorts.com/app/laporanBug.php";
    public static final String KEY_ENQ="http://aliyanaresorts.com/app/Enquiry.php";
    public static final String KEY_MENU_RESTO="http://aliyanaresorts.com/app/menuResto.php";
    public static final String KEY_MAIN_RESTO="http://aliyanaresorts.com/app/mainResto.php";

    public static String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            StringBuilder passBuilder = new StringBuilder(new BigInteger(1, mdEnc.digest()).toString(16));
            while (passBuilder.length() < 32) {
                passBuilder.insert(0, "0");
            }
            pass = passBuilder.toString();
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }

}
