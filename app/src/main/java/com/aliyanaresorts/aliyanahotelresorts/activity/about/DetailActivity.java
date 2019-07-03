package com.aliyanaresorts.aliyanahotelresorts.activity.about;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.view.WindowManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_TENTANG_HOTEL;
import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setWindowFlag;

public class DetailActivity extends AppCompatActivity {

    private CardView lokasi , email, wa, web, phone, fax, ig, fb;
    private TextView textView;

    private ArrayList<HashMap<String, String>> list_data;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(getResources().getColor(R.color.goldtrans));

        lokasi=findViewById(R.id.btLokasi);
        email=findViewById(R.id.btEmail);
        wa=findViewById(R.id.btWa);
        web=findViewById(R.id.btWeb);
        phone=findViewById(R.id.btPhone);
        fax=findViewById(R.id.btFax);
        ig=findViewById(R.id.btIg);
        fb=findViewById(R.id.btFb);
        textView=findViewById(R.id.textTentang);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mDialog = new ProgressDialog(DetailActivity.this);
        mDialog.setMessage(getResources().getString(R.string.tunggu));
        mDialog.show();
        mDialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(DetailActivity.this);

        list_data = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, KEY_TENTANG_HOTEL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("konfig");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", json.getString("id"));
                        map.put("tentang", json.getString("tentang"));
                        map.put("facebook", json.getString("facebook"));
                        map.put("fax", json.getString("fax"));
                        map.put("instagram", json.getString("instagram"));
                        map.put("mail1", json.getString("mail1"));
                        map.put("mail2", json.getString("mail2"));
                        map.put("phone", json.getString("phone"));
                        map.put("whatsapp", json.getString("whatsapp"));
                        map.put("web", json.getString("web"));
                        list_data.add(map);
                    }

                    mDialog.dismiss();

                    textView.setText(new SpannableString(list_data.get(0).get("tentang")));


                    lokasi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri location = Uri.parse("geo:0,0?q=Aliyana+Hotel+%26+Resort");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                            startActivity(mapIntent);
                        }
                    });

                    fb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri fb = Uri.parse(list_data.get(0).get("facebook"));
                            Intent kefb = new Intent(Intent.ACTION_VIEW, fb);
                            startActivity(kefb);
                        }
                    });

                    fax.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri number = Uri.parse("tel:" + list_data.get(0).get("fax"));
                            Intent faxIntent = new Intent(Intent.ACTION_DIAL, number);
                            startActivity(faxIntent);
                        }
                    });

                    ig.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri ig = Uri.parse(list_data.get(0).get("instagram"));
                            Intent keig = new Intent(Intent.ACTION_VIEW, ig);
                            startActivity(keig);
                        }
                    });

                    email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setType("text/plain");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{list_data.get(0)
                                    .get("mail1") + ";" + list_data.get(0).get("mail2")}); // recipients
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "About Aliyana Hotel & Resorts");
                            startActivity(emailIntent);
                        }
                    });

                    phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri number = Uri.parse("tel:" + list_data.get(0).get("phone"));
                            Intent faxIntent = new Intent(Intent.ACTION_DIAL, number);
                            startActivity(faxIntent);
                        }
                    });

                    wa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri wa = Uri.parse("https://api.whatsapp.com/send?phone="
                                    + list_data.get(0).get("whatsapp")
                                    + "&text=About Aliyana Hotel & Resorts");
                            Intent waIntent = new Intent(Intent.ACTION_VIEW, wa);
                            startActivity(waIntent);
                        }
                    });

                    web.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri web = Uri.parse(list_data.get(0).get("web"));
                            Intent keweb = new Intent(Intent.ACTION_VIEW, web);
                            startActivity(keweb);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue
                .add(stringRequest)
                .setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
}
