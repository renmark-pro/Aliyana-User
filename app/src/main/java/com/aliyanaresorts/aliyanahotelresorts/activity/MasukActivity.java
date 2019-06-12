package com.aliyanaresorts.aliyanahotelresorts.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.service.database.AppController;
import com.aliyanaresorts.aliyanahotelresorts.service.SPData;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setStyleStatusBarGoldTrans;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_MASUK;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_SLIDE_HOME;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.convertPassMd5;

public class MasukActivity extends AppCompatActivity {

    private FlipperLayout flipper;

    private ArrayList<HashMap<String, String>> list_dataS;

    private EditText mtelepon, mpassword;

    private int success;
    private ConnectivityManager conMgr;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk);
        setStyleStatusBarGoldTrans(this);

        flipper = findViewById(R.id.flipper);
        Button masuk = findViewById(R.id.btnMasuk);
        Button daftar = findViewById(R.id.btnDaftar);
        mtelepon= findViewById(R.id.telpon);
        mpassword= findViewById(R.id.password);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (Objects.requireNonNull(conMgr).getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        setSlide();

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telepon = mtelepon.getText().toString();
                String password = mpassword.getText().toString();

                if (telepon.isEmpty()) {
                    mtelepon.requestFocus();
                    mtelepon.setError(getResources().getString(R.string.isi));
                } else if (password.isEmpty()) {
                    mpassword.setError(getResources().getString(R.string.isi));
                    mpassword.requestFocus();
                }else  {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkLogin(telepon, password, v);
                    } else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MasukActivity.this, DaftarActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkLogin(final String telepon, final String password, final View view) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, KEY_MASUK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(MasukActivity.class.getSimpleName(), "Login Response: " + response);
                pDialog.dismiss();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt("success");

                    // Check for error node in json
                    if (success == 1) {
                        SPData.getInstance(getApplicationContext()).userLogin(
                                jObj.getString("uid"),
                                jObj.getString("nama"),
                                jObj.getString("jenis_id"),
                                jObj.getString("nomer_id"),
                                jObj.getString("foto"),
                                jObj.getString("alamat"),
                                jObj.getString("email"),
                                jObj.getString("kota"),
                                jObj.getString("negara"),
                                jObj.getString("telepon"),
                                jObj.getString("password")
                        );
                        Snackbar.make(view, R.string.bmasuk, Snackbar.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);
                    } else {
                        Snackbar.make(view, R.string.xmasuk, Snackbar.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(MasukActivity.class.getSimpleName(), "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                pDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("telepon", telepon);
                params.put("password", convertPassMd5(password));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }

    private void setSlide() {
        RequestQueue requestQueueS = Volley.newRequestQueue(MasukActivity.this);

        list_dataS = new ArrayList<>();

        StringRequest stringRequestS = new StringRequest(Request.Method.GET, KEY_SLIDE_HOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject json = jsonArray.getJSONObject(a);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id", json.getString("id"));
                                map.put("foto", json.getString("foto"));
                                map.put("nama", json.getString("nama"));
                                list_dataS.add(map);
                                FlipperView view = new FlipperView(getBaseContext());
                                view.setImageUrl(list_dataS.get(a).get("foto"))
                                        .setDescription(list_dataS.get(a).get("nama"))
                                        .setDescriptionBackgroundColor(getResources()
                                                .getColor(R.color.hitamtrans));
                                flipper.setCircularIndicatorLayoutParams(0,0);
                                flipper.addFlipperView(view);
                                final int finalA = a;
                                view.setOnFlipperClickListener(new FlipperView.OnFlipperClickListener() {
                                    @Override
                                    public void onFlipperClick(FlipperView flipperView) {
                                        Snackbar.make(flipper, Objects.requireNonNull(list_dataS.get(finalA).get("nama")), Snackbar.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MasukActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueueS
                .add(stringRequestS)
                .setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
