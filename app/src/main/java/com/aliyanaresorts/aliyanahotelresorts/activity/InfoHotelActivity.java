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
import android.widget.ImageView;

import com.aliyanaresorts.aliyanahotelresorts.R;

import java.util.Objects;

public class InfoHotelActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView ballroom, meeting, resto, spa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_hotel);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        @SuppressLint("PrivateResource") final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        Objects.requireNonNull(upArrow).setColorFilter(ContextCompat.getColor(this, R.color.putih), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        ballroom= findViewById(R.id.bt_ball);
        meeting= findViewById(R.id.bt_meet);
        resto= findViewById(R.id.bt_resto);
        spa= findViewById(R.id.bt_spa);

        ballroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Holder
            }
        });

        meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Holder
            }
        });

        resto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoHotelActivity.this, RestoActivity.class);
                startActivity(intent);
            }
        });

    }
}
