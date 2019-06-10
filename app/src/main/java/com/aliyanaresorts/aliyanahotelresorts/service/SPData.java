package com.aliyanaresorts.aliyanahotelresorts.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SPData {

        @SuppressLint("StaticFieldLeak")
        private static SPData mInstance;
        @SuppressLint("StaticFieldLeak")
        private static Context mCtx;

        private static final String SHARED_PREF_NAME = "mysharedpref12";
        private static final String KEY_UID = "uid";
        private static final String KEY_NAMA = "nama";
        private static final String KEY_JENIS_ID = "jenis_id";
        private static final String KEY_NOMER_ID = "nomer_id";
        private static final String KEY_FOTO = "foto";
        private static final String KEY_ALAMAT = "alamat";
        private static final String KEY_EMAIL = "email";
        private static final String KEY_KOTA = "kota";
        private static final String KEY_NEGARA = "negara";
        private static final String KEY_TELEPON = "telepon";
        private static final String KEY_PASSWORD = "password";

        private SPData(Context context) {
                mCtx = context;

        }

        public static synchronized SPData getInstance(Context context) {
                if (mInstance == null) {
                        mInstance = new SPData(context);
                }
                return mInstance;
        }

        public void userLogin(String uid, String nama, String jenis_id, String nomer_id, String foto, String alamat, String email, String kota, String negara, String telepon, String password){

                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(KEY_UID, uid);
                editor.putString(KEY_NAMA, nama);
                editor.putString(KEY_JENIS_ID, jenis_id);
                editor.putString(KEY_NOMER_ID, nomer_id);
                editor.putString(KEY_FOTO, foto);
                editor.putString(KEY_ALAMAT, alamat);
                editor.putString(KEY_EMAIL, email);
                editor.putString(KEY_KOTA, kota);
                editor.putString(KEY_NEGARA, negara);
                editor.putString(KEY_TELEPON, telepon);
                editor.putString(KEY_PASSWORD, password);

                editor.apply();

        }

        public void updateFoto(String foto){

                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(KEY_FOTO, foto);

                editor.apply();
        }


        public boolean isLoggedIn(){
                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_UID, null) != null;
        }

        public void logout(){
                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                editor.commit();
        }

        public String getKeyUid() {
                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                return sharedPreferences.getString(KEY_UID, null);
        }

        public String getKeyNama() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_NAMA, null);
        }

//        public String getKeyJenisId() {
//            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//            return sharedPreferences.getString(KEY_JENIS_ID, null);
//        }
//
//        public String getKeyNomerId() {
//            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//            return sharedPreferences.getString(KEY_NOMER_ID, null);
//        }

        public String getKeyFoto() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_FOTO, null);
        }

        public String getKeyAlamat() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_ALAMAT, null);
        }

//        public String getKeyEmail() {
//            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//            return sharedPreferences.getString(KEY_EMAIL, null);
//        }
//
//        public String getKeyKota() {
//            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//            return sharedPreferences.getString(KEY_KOTA, null);
//        }
//
//        public String getKeyNegara() {
//            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//            return sharedPreferences.getString(KEY_NEGARA, null);
//        }

        public String getKeyTelepon() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_TELEPON, null);
        }

//        public String getKeyPassword() {
//            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//            return sharedPreferences.getString(KEY_PASSWORD, null);
//        }
}
