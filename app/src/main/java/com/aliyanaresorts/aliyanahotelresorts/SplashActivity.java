package com.aliyanaresorts.aliyanahotelresorts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setStyleStatusBarGoldTrans;
import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setStyleStatusBarTransparent;
import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setWindowFlag;

public class SplashActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_GET_ACCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setStyleStatusBarGoldTrans(this);

        getPermissions();

    }

    private void lanjutIntent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent masuk = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(masuk);
                finish();
            }
        }, 2000);
    }

    public void getPermissions() {
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_GET_ACCESS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_GET_ACCESS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lanjutIntent();
            } else {
                Toast.makeText(SplashActivity.this, "Ijin Di Tolak!", Toast.LENGTH_SHORT).show();
                lanjutIntent();
            }
        }
    }

}
