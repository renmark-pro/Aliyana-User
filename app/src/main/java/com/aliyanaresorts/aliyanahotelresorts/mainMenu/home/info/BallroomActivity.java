package com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.info;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import static com.aliyanaresorts.aliyanahotelresorts.tools.Style.setStyleStatusBarTransparent;

public class BallroomActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ballroom);
        setStyleStatusBarTransparent(this);

        Button detail = findViewById(R.id.bt_detail);
        Button book = findViewById(R.id.bt_book);

        collapsingToolbarLayout = findViewById(R.id.coll);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
                    //  on Collapse
                    collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);
                    collapsingToolbarLayout.setTitle(getResources().getString(R.string.ballali));
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.putih));
                    collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.transparan));
                    collapsingToolbarLayout.setContentScrim(new ColorDrawable(getResources().getColor(R.color.hitamtrans)));
                } else {
                    collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);
                    collapsingToolbarLayout.setTitle("\t");
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.putih));
                }
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Holder
            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BallroomActivity.this, BallroomDetailActivity.class));
            }
        });

    }
}
