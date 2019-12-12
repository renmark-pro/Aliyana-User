package com.aliyanaresorts.aliyanahotelresorts.mainMenu.account;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aliyanaresorts.aliyanahotelresorts.R;

import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.getStatusBarHeight;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Style.setStyleStatusBarGoldTrans;

public class PromoAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_account);


        RelativeLayout root = findViewById(R.id.layoutRoot);
        root.setPadding(16, getStatusBarHeight(this)+16, 16 , 16 );
        setStyleStatusBarGoldTrans(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
