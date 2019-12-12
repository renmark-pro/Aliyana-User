package com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.rooms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.dialog.LoadingDialog;
import com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.booking.BookingActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyDomainSistem;
import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyKamarDetail;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.formatingRupiah;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.getIntentData;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Style.setTemaAplikasi;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class RoomDetailActivity extends AppCompatActivity {

    private ImageView fotoKamar;
    private TextView hargaKamar,deskripsiKamar,fasilitasKamar;
    private Button button;

    private LoadingDialog loadingDialog;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipe;
    
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        setTemaAplikasi(this, 0);

        activity = this;
        loadingDialog = new LoadingDialog(this);
        fotoKamar = findViewById(R.id.fotoKamar);
        hargaKamar = findViewById(R.id.hargaKamar);
        deskripsiKamar = findViewById(R.id.deskripsiKamar);
        fasilitasKamar = findViewById(R.id.fasilitasKamar);
        button = findViewById(R.id.bt_book);
        swipe = findViewById(R.id.swipeLayout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        @SuppressLint("PrivateResource") final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        Objects.requireNonNull(upArrow).setColorFilter(ContextCompat.getColor(this, R.color.putih), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        getData();
        Resources res = getResources();
        swipe.setColorSchemeColors(res.getColor(R.color.colorPrimaryDark), res.getColor(R.color.colorPrimary));
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipe.setRefreshing(false);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, BookingActivity.class);
                intent.putExtra("posisi", getIntentData(activity,"posisi"));
                startActivity(intent);
            }
        });
    }

    private void getData() {
        loadingDialog.bukaDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, keyKamarDetail +
                getIntentData(this,"posisi"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject kamar = new JSONObject(response);
                    loadingDialog.tutupDialog();
                    Glide.with(activity).load(keyDomainSistem+kamar.getString("foto"))
                            .placeholder(R.drawable.image_slider_1)
                            .thumbnail(0.5f)
                            .transition(withCrossFade())
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(fotoKamar);
                    deskripsiKamar.setText(kamar.getString("deskripsi"));
                    fasilitasKamar.setText(kamar.getString("fasilitas"));
                    toolbar.setTitle(kamar.getString("nama"));
                    hargaKamar.setText(formatingRupiah(kamar.getString("harga")));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, BookingActivity.class);
                            intent.putExtra("posisi", toolbar.getTitle());
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue
                .add(stringRequest)
                .setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
