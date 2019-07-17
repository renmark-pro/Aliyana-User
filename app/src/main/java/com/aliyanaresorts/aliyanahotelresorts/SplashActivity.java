package com.aliyanaresorts.aliyanahotelresorts;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.getPermissions;
import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setStyleStatusBarGoldTrans;

public class SplashActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_GET_ACCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setStyleStatusBarGoldTrans(this);

        getPermissions(SplashActivity.this);

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri ali = appLinkIntent.getData();
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
