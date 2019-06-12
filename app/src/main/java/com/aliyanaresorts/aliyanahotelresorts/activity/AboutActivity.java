package com.aliyanaresorts.aliyanahotelresorts.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.aliyanaresorts.aliyanahotelresorts.activity.about.BugActivity;
import com.aliyanaresorts.aliyanahotelresorts.activity.about.DetailActivity;
import com.aliyanaresorts.aliyanahotelresorts.activity.about.EnquiryActivity;
import com.aliyanaresorts.aliyanahotelresorts.activity.about.VisiActivity;
import com.aliyanaresorts.aliyanahotelresorts.R;

import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setTemaAplikasi;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTemaAplikasi(AboutActivity.this, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        @SuppressLint("PrivateResource") final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        Objects.requireNonNull(upArrow).setColorFilter(ContextCompat.getColor(this, R.color.goldtua), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        RelativeLayout visi = findViewById(R.id.btVisi);
        RelativeLayout kontak = findViewById(R.id.btKontak);
        RelativeLayout enquir = findViewById(R.id.btEnq);
        RelativeLayout bug = findViewById(R.id.btBug);

        visi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, VisiActivity.class);
                startActivity(intent);
            }
        });

        kontak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });

        enquir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, EnquiryActivity.class);
                startActivity(intent);
            }
        });

        bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, BugActivity.class);
                startActivity(intent);
            }
        });
    }
}
