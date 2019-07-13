package com.aliyanaresorts.aliyanahotelresorts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aliyanaresorts.aliyanahotelresorts.activity.MasukActivity;
import com.aliyanaresorts.aliyanahotelresorts.activity.fragment.AccountFragment;
import com.aliyanaresorts.aliyanahotelresorts.activity.fragment.HomeFragment;
import com.aliyanaresorts.aliyanahotelresorts.activity.fragment.StatusFragment;
import com.aliyanaresorts.aliyanahotelresorts.service.NoInetDialog;
import com.aliyanaresorts.aliyanahotelresorts.service.SPData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.isNetworkAvailable;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private int HOME=0;

    private static BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            HOME=0;
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    new HomeFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    private final AccountFragment accountFragment = new AccountFragment();
    private final HomeFragment homeFragment = new HomeFragment();
    private final StatusFragment statusFragment = new StatusFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        NoInetDialog noInetDialog = new NoInetDialog(this);

        switch (menuItem.getItemId()){
            case R.id.navigation_account:
                if (SPData.getInstance(this).isLoggedIn()) {
                    HOME = 1;
                    getSupportFragmentManager().beginTransaction().detach(accountFragment).attach(accountFragment).setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, accountFragment).commit();
                }else {
                    Intent intent = new Intent(MainActivity.this, MasukActivity.class);
                    startActivity(intent);
                }
                return true;
            case R.id.navigation_home:
                HOME=0;
                getSupportFragmentManager().beginTransaction().detach(homeFragment).attach(homeFragment).setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, homeFragment).commit();
                return true;
            case R.id.navigation_status:
                if (!isNetworkAvailable(getBaseContext())){
                    noInetDialog.bukaDialog();
                }else {
                    HOME = 1;
                    getSupportFragmentManager().beginTransaction().detach(statusFragment).attach(statusFragment).setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, statusFragment).commit();
                    return true;
                }
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        if (HOME!=0) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage(R.string.alert_keluar);
            builder.setPositiveButton(R.string.alert_ya, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton(R.string.alert_tidak, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

}
