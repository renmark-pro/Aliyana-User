package com.aliyanaresorts.aliyanahotelresorts.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SPData {
        @SuppressLint("StaticFieldLeak")
        private static SPData mInstance;
        @SuppressLint("StaticFieldLeak")
        private static Context mCtx;

        private static final String sharedPrefName = "aliyana";
        private static final String keyId = "id";
        private static final String keyNama = "nama";
        private static final String keyEmail = "email";
        private static final String keyJenisId = "tipe_identitas";
        private static final String keyNomerId = "no_identitas";
        private static final String keyKdTelepon = "kd_telepon";
        private static final String keyTelepon = "no_telepon";
        private static final String keyAlamat = "alamat";
        private static final String keyFoto = "foto";
        private static final String keyToken = "access_token";

        private SPData(Context context) {
                mCtx = context;

        }

        public static synchronized SPData getInstance(Context context) {
                if (mInstance == null) {
                        mInstance = new SPData(context);
                }
                return mInstance;
        }

        public void userLogin(String id, String nama, String email, String jenis_id, String nomer_id, String kd_telpon, String telepon, String alamat, String foto, String token){

                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(keyId, id);
                editor.putString(keyNama, nama);
                editor.putString(keyEmail, email);
                editor.putString(keyJenisId, jenis_id);
                editor.putString(keyNomerId, nomer_id);
                editor.putString(keyKdTelepon, kd_telpon);
                editor.putString(keyTelepon, telepon);
                editor.putString(keyAlamat, alamat);
                editor.putString(keyFoto, foto);
                editor.putString(keyToken, token);

                editor.apply();

        }

        public void updateBio(String nama, String email, String tipe, String id, String kd_telpon, String telpon, String alamat){
                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(keyNama, nama);
                editor.putString(keyEmail, email);
                editor.putString(keyJenisId, tipe);
                editor.putString(keyNomerId, id);
                editor.putString(keyKdTelepon, kd_telpon);
                editor.putString(keyTelepon, telpon);
                editor.putString(keyAlamat, alamat);

                editor.apply();
        }

        public void updateFoto(String foto){

                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(keyFoto, foto);

                editor.apply();
        }


        public boolean isLoggedIn(){
                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
            return sharedPreferences.getString(keyToken, null) != null;
        }

        public void logout(){
                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                editor.commit();
        }

        public String getKeyToken() {
                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
                return sharedPreferences.getString(keyToken, null);
        }

        public String getKeyNama() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
            return sharedPreferences.getString(keyNama, null);
        }

        public String getKeyJenisId() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
            return sharedPreferences.getString(keyJenisId, null);
        }

        public String getKeyNomerId() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
            return sharedPreferences.getString(keyNomerId, null);
        }

        public String getKeyFoto() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
            return sharedPreferences.getString(keyFoto, null);
        }

        public String getKeyAlamat() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
            return sharedPreferences.getString(keyAlamat, null);
        }

        public String getKeyEmail() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
            return sharedPreferences.getString(keyEmail, null);
        }

        public String getKeyKdTelepon() {
                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
                return sharedPreferences.getString(keyKdTelepon, null);
        }

        public String getKeyTelepon() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
            return sharedPreferences.getString(keyTelepon, null);
        }
}
