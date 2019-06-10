package com.aliyanaresorts.aliyanahotelresorts.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_DOMAIN;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_KAMAR_DETAIL;
import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setTemaAplikasi;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class RoomDetailActivity extends AppCompatActivity {

    ImageView fotoKamar;
    TextView hargaKamar,deskripsiKamar,fasilitasKamar;
    Button button;

    ArrayList<HashMap<String, String>> list_data;

    ProgressDialog mDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        setTemaAplikasi(RoomDetailActivity.this, 0);

        fotoKamar = findViewById(R.id.fotoKamar);
        hargaKamar = findViewById(R.id.hargaKamar);
        deskripsiKamar = findViewById(R.id.deskripsiKamar);
        fasilitasKamar = findViewById(R.id.fasilitasKamar);
        button = findViewById(R.id.bt_book);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        @SuppressLint("PrivateResource") final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        Objects.requireNonNull(upArrow).setColorFilter(ContextCompat.getColor(this, R.color.putih), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        mDialog = new ProgressDialog(RoomDetailActivity.this);
        mDialog.setMessage(getResources().getString(R.string.tunggu));
        mDialog.show();
        mDialog.setCancelable(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomDetailActivity.this, BookingActivity.class);
                intent.putExtra("posisi", getIntent().getStringExtra("posisi"));
                startActivity(intent);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(RoomDetailActivity.this);

        list_data = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, KEY_KAMAR_DETAIL +
                getIntent().getStringExtra("posisi"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("kamar");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", json.getString("id"));
                        map.put("deskripsi", json.getString("deskripsi"));
                        map.put("fasilitas", json.getString("fasilitas"));
                        map.put("foto", json.getString("foto"));
                        map.put("nama", json.getString("nama"));
                        map.put("harga", json.getString("harga"));
                        list_data.add(map);
                    }
                    mDialog.dismiss();
                    Glide.with(RoomDetailActivity.this).load(KEY_DOMAIN+list_data.get(0).get("foto"))
                            .placeholder(R.drawable.image_slider_1)
                            .thumbnail(0.5f)
                            .transition(withCrossFade())
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(fotoKamar);
                    deskripsiKamar.setText(list_data.get(0).get("deskripsi"));
                    fasilitasKamar.setText(list_data.get(0).get("fasilitas"));
                    toolbar.setTitle(list_data.get(0).get("nama"));
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    hargaKamar.setText(formatRupiah.format((double)Double.valueOf(Objects.requireNonNull(list_data.get(0).get("harga")))));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(RoomDetailActivity.this, BookingActivity.class);
                            intent.putExtra("posisi", list_data.get(0).get("id"));
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
                Toast.makeText(RoomDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue
                .add(stringRequest)
                .setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
