package com.aliyanaresorts.aliyanahotelresorts.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setStyleStatusBarTransparent;

public class MeetingActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        setStyleStatusBarTransparent(this);

        collapsingToolbarLayout = findViewById(R.id.coll);
        appBarLayout = findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
                    //  on Collapse
                    collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);
                    collapsingToolbarLayout.setTitle(getResources().getString(R.string.meetroom));
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.putih));
                } else {
                    collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);
                    collapsingToolbarLayout.setTitle("\t");
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.putih));
                }
            }
        });

    }
}
