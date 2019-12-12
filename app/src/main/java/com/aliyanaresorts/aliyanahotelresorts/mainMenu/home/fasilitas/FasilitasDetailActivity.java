package com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.fasilitas;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyDomainSistem;
import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyFasilitasDetail;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.getIntentData;

public class FasilitasDetailActivity extends AppCompatActivity {

    private FlipperLayout flipper;
    private ArrayList<HashMap<String, String>> list_dataS;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fasilitas_detail);

        toolbar= findViewById(R.id.toolbar);
        flipper=findViewById(R.id.flipper);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        @SuppressLint("PrivateResource") final Drawable upArrow = ContextCompat.
                getDrawable(this, R.drawable.abc_ic_ab_back_material);
        Objects.requireNonNull(upArrow).setColorFilter(ContextCompat.getColor(this,
                R.color.putih), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        setSlide();
    }

    private void setSlide() {
        RequestQueue requestQueueS = Volley.newRequestQueue(this);
        list_dataS = new ArrayList<>();
        StringRequest stringRequestS = new StringRequest(Request.Method.GET, keyFasilitasDetail
                +getIntentData(this,"posisi"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("fasilitas");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject json = jsonArray.getJSONObject(a);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("lokasi", json.getString("lokasi"));
                                map.put("nama", json.getString("nama"));
                                list_dataS.add(map);
                                toolbar.setTitle(list_dataS.get(0).get("nama"));
                                toolbar.setTitleTextColor(getResources().getColor(R.color.putih));
                                FlipperView view = new FlipperView(getBaseContext());
                                view.setImageUrl(keyDomainSistem+list_dataS.get(a).get("lokasi"))
                                .setDescription((a+1)+"/"+jsonArray.length())
                                .setDescriptionBackgroundColor(getResources().getColor(R.color.goldtrans))
                                .setImageScaleType(ImageView.ScaleType.FIT_CENTER);
                                flipper.setScrollTimeInSec(10);
                                flipper.setCircularIndicatorLayoutParams(0,0);
                                flipper.addFlipperView(view);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueueS
                .add(stringRequestS)
                .setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
